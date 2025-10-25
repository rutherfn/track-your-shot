#!/bin/bash

# Script to automatically generate mock statements for StringsIds
# This script parses strings.xml and non_translatable.xml files and generates
# the mock statements for BaseTest.kt

echo "🔍 Generating string mocks for BaseTest..."

# Set up paths
PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
BASE_RESOURCES_PATH="$PROJECT_ROOT/base-resources/src/main/res/values"
STRINGS_FILE="$BASE_RESOURCES_PATH/strings.xml"
NON_TRANSLATABLE_FILE="$BASE_RESOURCES_PATH/non_translatable.xml"
STRINGS_IDS_FILE="$PROJECT_ROOT/base-resources/src/main/java/com/nicholas/rutherford/track/your/shot/base/resources/StringsIds.kt"
OUTPUT_FILE="$PROJECT_ROOT/scripts/generated-mocks.kt"

# Check if files exist
if [ ! -f "$STRINGS_FILE" ]; then
    echo "❌ strings.xml not found at: $STRINGS_FILE"
    exit 1
fi

if [ ! -f "$NON_TRANSLATABLE_FILE" ]; then
    echo "❌ non_translatable.xml not found at: $NON_TRANSLATABLE_FILE"
    exit 1
fi

if [ ! -f "$STRINGS_IDS_FILE" ]; then
    echo "❌ StringsIds.kt not found at: $STRINGS_IDS_FILE"
    exit 1
fi

echo "📝 Parsing XML files..."

# Create a temporary Python script to parse the XML files
cat > /tmp/parse_strings.py << 'EOF'
import xml.etree.ElementTree as ET
import re
import sys

def parse_xml_file(file_path):
    """Parse XML file and extract string name-value pairs"""
    try:
        tree = ET.parse(file_path)
        root = tree.getroot()
        strings = {}
        
        for string_elem in root.findall('string'):
            name = string_elem.get('name')
            if name:
                # Get text content and handle CDATA
                text = string_elem.text or ''
                # Clean up the text
                text = text.replace('\n', '\\n').replace('\t', '\\t').replace('\r', '\\r')
                text = text.replace('"', '\\"').replace('\\', '\\\\')
                strings[name] = text
        
        return strings
    except Exception as e:
        print(f"Error parsing {file_path}: {e}")
        return {}

def parse_strings_ids(file_path):
    """Parse StringsIds.kt file to extract property names"""
    try:
        with open(file_path, 'r') as f:
            content = f.read()
        
        # Find all val propertyName = R.string.string_name patterns
        pattern = r'val\s+(\w+)\s*=\s*R\.string\.(\w+)'
        matches = re.findall(pattern, content)
        
        return [(prop, string) for prop, string in matches]
    except Exception as e:
        print(f"Error parsing {file_path}: {e}")
        return []

def main():
    strings_file = sys.argv[1]
    non_translatable_file = sys.argv[2]
    strings_ids_file = sys.argv[3]
    
    # Parse XML files
    strings_map = parse_xml_file(strings_file)
    non_translatable_map = parse_xml_file(non_translatable_file)
    
    # Combine (non_translatable takes precedence)
    all_strings = {**strings_map, **non_translatable_map}
    
    # Parse StringsIds
    properties = parse_strings_ids(strings_ids_file)
    
    # Generate mock statements
    mock_statements = []
    missing_strings = []
    
    for prop_name, string_name in properties:
        if string_name in all_strings:
            value = all_strings[string_name]
            mock_statement = f'every {{ application.getString(StringsIds.{prop_name}) }} returns "{value}"'
            mock_statements.append(mock_statement)
        else:
            mock_statement = f'every {{ application.getString(StringsIds.{prop_name}) }} returns "MISSING: {string_name}"'
            mock_statements.append(mock_statement)
            missing_strings.append(string_name)
    
    # Output results
    print(f"Found {len(all_strings)} string resources")
    print(f"Found {len(properties)} StringsIds properties")
    
    if missing_strings:
        print(f"⚠️  Missing strings: {', '.join(missing_strings)}")
    
    # Write to output file
    with open('/tmp/generated_mocks.kt', 'w') as f:
        f.write("private fun mockApplicationStrings() {\n")
        for statement in mock_statements:
            f.write(f"    {statement}\n")
        f.write("}\n")
    
    print(f"✅ Generated {len(mock_statements)} mock statements")
    print(f"📄 Output written to: /tmp/generated_mocks.kt")

if __name__ == "__main__":
    main()
EOF

# Run the Python script
python3 /tmp/parse_strings.py "$STRINGS_FILE" "$NON_TRANSLATABLE_FILE" "$STRINGS_IDS_FILE"

if [ $? -eq 0 ]; then
    echo ""
    echo "🔧 Updating BaseTest.kt file..."
    
    # Find the BaseTest.kt file
    BASE_TEST_FILE="$PROJECT_ROOT/base/test-ext/src/main/java/com/nicholas/rutherford/track/your/shot/base/test/BaseTest.kt"
    
    if [ ! -f "$BASE_TEST_FILE" ]; then
        echo "❌ BaseTest.kt not found at: $BASE_TEST_FILE"
        exit 1
    fi
    
    # Create a backup
    cp "$BASE_TEST_FILE" "$BASE_TEST_FILE.backup"
    echo "📁 Created backup: $BASE_TEST_FILE.backup"
    
    # Update the BaseTest.kt file
    python3 /tmp/update_base_test.py "$BASE_TEST_FILE" /tmp/generated_mocks.kt
    
    if [ $? -eq 0 ]; then
        echo "✅ Successfully updated BaseTest.kt!"
        echo "🎉 All string mocks have been automatically generated and applied!"
    else
        echo "❌ Failed to update BaseTest.kt"
        echo "🔄 Restoring from backup..."
        mv "$BASE_TEST_FILE.backup" "$BASE_TEST_FILE"
        exit 1
    fi
else
    echo "❌ Failed to generate mock statements"
    exit 1
fi

# Create the file updater script
cat > /tmp/update_base_test.py << 'EOF'
import re
import sys

def update_base_test_file(base_test_file, mock_statements_file):
    """Update the BaseTest.kt file with new mock statements"""
    try:
        # Read the current BaseTest.kt file
        with open(base_test_file, 'r') as f:
            content = f.read()
        
        # Read the generated mock statements
        with open(mock_statements_file, 'r') as f:
            mock_content = f.read()
        
        # Find the mockApplicationStrings method
        # Look for the method signature and replace everything until the closing brace
        pattern = r'(private fun mockApplicationStrings\(\) \{[^}]*)(\})'
        
        def replace_method(match):
            method_start = match.group(1)
            closing_brace = match.group(2)
            
            # Extract the mock statements (everything between the braces)
            mock_statements = mock_content.strip()
            if mock_statements.startswith('private fun mockApplicationStrings() {'):
                # Remove the method signature from mock_statements
                mock_statements = mock_statements.replace('private fun mockApplicationStrings() {', '').strip()
                if mock_statements.endswith('}'):
                    mock_statements = mock_statements[:-1].strip()
            
            # Reconstruct the method
            new_method = f"private fun mockApplicationStrings() {{\n{mock_statements}\n{closing_brace}"
            return new_method
        
        # Replace the method
        new_content = re.sub(pattern, replace_method, content, flags=re.DOTALL)
        
        if new_content == content:
            print("⚠️  No changes made - method not found or already up to date")
            return False
        
        # Write the updated content back to the file
        with open(base_test_file, 'w') as f:
            f.write(new_content)
        
        print("✅ Successfully updated BaseTest.kt")
        return True
        
    except Exception as e:
        print(f"❌ Error updating BaseTest.kt: {e}")
        return False

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python3 update_base_test.py <base_test_file> <mock_statements_file>")
        sys.exit(1)
    
    base_test_file = sys.argv[1]
    mock_statements_file = sys.argv[2]
    
    success = update_base_test_file(base_test_file, mock_statements_file)
    sys.exit(0 if success else 1)
EOF

# Clean up
rm -f /tmp/parse_strings.py /tmp/generated_mocks.kt /tmp/update_base_test.py
