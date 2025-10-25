#!/usr/bin/env kotlin

import java.io.File
import java.util.regex.Pattern

/**
 * Script to automatically generate mock statements for StringsIds by parsing XML files.
 * 
 * Usage:
 * 1. Run this script from the project root: kotlin scripts/generate-string-mocks.kt
 * 2. It will parse strings.xml and non_translatable.xml
 * 3. Generate the mock statements for BaseTest.kt
 * 4. Output the generated code to console (you can copy-paste it)
 */

fun main() {
    val projectRoot = File(".")
    val baseResourcesPath = "base-resources/src/main/res/values"
    val stringsFile = File(projectRoot, "$baseResourcesPath/strings.xml")
    val nonTranslatableFile = File(projectRoot, "$baseResourcesPath/non_translatable.xml")
    val stringsIdsFile = File(projectRoot, "base-resources/src/main/java/com/nicholas/rutherford/track/your/shot/base/resources/StringsIds.kt")
    
    println("🔍 Parsing string resources...")
    
    // Parse both XML files
    val stringsMap = parseXmlFile(stringsFile)
    val nonTranslatableMap = parseXmlFile(nonTranslatableFile)
    
    // Combine both maps (non_translatable takes precedence)
    val allStrings = stringsMap + nonTranslatableMap
    
    println("📝 Found ${allStrings.size} string resources")
    
    // Parse StringsIds.kt to get the property names
    val stringsIdsProperties = parseStringsIdsFile(stringsIdsFile)
    
    println("🎯 Found ${stringsIdsProperties.size} StringsIds properties")
    
    // Generate mock statements
    val mockStatements = generateMockStatements(stringsIdsProperties, allStrings)
    
    // Update BaseTest.kt file directly
    val baseTestFile = File(projectRoot, "base/test-ext/src/main/java/com/nicholas/rutherford/track/your/shot/base/test/BaseTest.kt")
    
    if (baseTestFile.exists()) {
        println("🔧 Updating BaseTest.kt file...")
        
        // Create backup
        val backupFile = File(baseTestFile.absolutePath + ".backup")
        baseTestFile.copyTo(backupFile, overwrite = true)
        println("📁 Created backup: ${backupFile.absolutePath}")
        
        // Update the file
        val success = updateBaseTestFile(baseTestFile, mockStatements)
        
        if (success) {
            println("✅ Successfully updated BaseTest.kt!")
            println("🎉 All string mocks have been automatically generated and applied!")
        } else {
            println("❌ Failed to update BaseTest.kt")
            println("🔄 Restoring from backup...")
            backupFile.copyTo(baseTestFile, overwrite = true)
        }
    } else {
        println("❌ BaseTest.kt not found at: ${baseTestFile.absolutePath}")
        println("\n" + "=".repeat(80))
        println("📋 GENERATED MOCK STATEMENTS")
        println("=".repeat(80))
        println()
        println("Copy the following code and replace the mockApplicationStrings() method in BaseTest.kt:")
        println()
        println("private fun mockApplicationStrings() {")
        mockStatements.forEach { println("    $it") }
        println("}")
        println()
        println("=".repeat(80))
    }
}

/**
 * Parse XML file and extract string name-value pairs
 */
fun parseXmlFile(file: File): Map<String, String> {
    if (!file.exists()) {
        println("⚠️  File not found: ${file.absolutePath}")
        return emptyMap()
    }
    
    val content = file.readText()
    val strings = mutableMapOf<String, String>()
    
    // Regex to match <string name="key">value</string> or <string name="key" translatable="false">value</string>
    val pattern = Pattern.compile("""<string\s+name="([^"]+)"(?:\s+translatable="false")?>(.*?)</string>""", Pattern.DOTALL)
    val matcher = pattern.matcher(content)
    
    while (matcher.find()) {
        val name = matcher.group(1)
        val value = matcher.group(2)
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&amp;", "&")
            .replace("&quot;", "\"")
            .replace("&apos;", "'")
            .replace("\\n", "\n")
            .replace("\\t", "\t")
            .replace("\\r", "\r")
            .replace("\\\"", "\"")
            .replace("\\\\", "\\")
        
        strings[name] = value
    }
    
    return strings
}

/**
 * Parse StringsIds.kt file to extract property names
 */
fun parseStringsIdsFile(file: File): List<String> {
    if (!file.exists()) {
        println("⚠️  File not found: ${file.absolutePath}")
        return emptyList()
    }
    
    val content = file.readText()
    val properties = mutableListOf<String>()
    
    // Regex to match val propertyName = R.string.string_name
    val pattern = Pattern.compile("""val\s+(\w+)\s*=\s*R\.string\.(\w+)""")
    val matcher = pattern.matcher(content)
    
    while (matcher.find()) {
        val propertyName = matcher.group(1)
        val stringName = matcher.group(2)
        properties.add("$propertyName:$stringName")
    }
    
    return properties
}

/**
 * Generate mock statements for each StringsIds property
 */
fun generateMockStatements(properties: List<String>, stringValues: Map<String, String>): List<String> {
    val mockStatements = mutableListOf<String>()
    
    properties.forEach { property ->
        val (propertyName, stringName) = property.split(":")
        val stringValue = stringValues[stringName]
        
        if (stringValue != null) {
            // Escape the string value for Kotlin
            val escapedValue = stringValue
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\t", "\\t")
                .replace("\r", "\\r")
            
            val mockStatement = "every { application.getString(StringsIds.$propertyName) } returns \"$escapedValue\""
            mockStatements.add(mockStatement)
        } else {
            // If string not found, add a placeholder
            val mockStatement = "every { application.getString(StringsIds.$propertyName) } returns \"MISSING: $stringName\""
            mockStatements.add(mockStatement)
            println("⚠️  String not found: $stringName")
        }
    }
    
    return mockStatements
}

/**
 * Update the BaseTest.kt file with new mock statements
 */
fun updateBaseTestFile(baseTestFile: File, mockStatements: List<String>): Boolean {
    return try {
        val content = baseTestFile.readText()
        
        // Find the mockApplicationStrings method and replace it
        val methodStart = "private fun mockApplicationStrings() {"
        val methodEnd = "    }"
        
        val startIndex = content.indexOf(methodStart)
        if (startIndex == -1) {
            println("⚠️  mockApplicationStrings method not found in BaseTest.kt")
            return false
        }
        
        // Find the end of the method (look for the closing brace with proper indentation)
        val afterMethodStart = content.substring(startIndex + methodStart.length)
        val endIndex = afterMethodStart.indexOf(methodEnd)
        
        if (endIndex == -1) {
            println("⚠️  Could not find end of mockApplicationStrings method")
            return false
        }
        
        val beforeMethod = content.substring(0, startIndex)
        val afterMethod = afterMethodStart.substring(endIndex + methodEnd.length)
        
        // Build the new method content
        val newMethodContent = buildString {
            append(methodStart)
            append("\n")
            mockStatements.forEach { statement ->
                append("        $statement\n")
            }
            append(methodEnd)
        }
        
        val newContent = beforeMethod + newMethodContent + afterMethod
        
        baseTestFile.writeText(newContent)
        true
    } catch (e: Exception) {
        println("❌ Error updating BaseTest.kt: ${e.message}")
        false
    }
}
