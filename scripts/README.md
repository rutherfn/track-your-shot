# String Mock Generator

This script automatically generates mock statements for `StringsIds` by parsing your XML string resource files.

## 🚀 Usage

### Option 1: Gradle Task (Recommended)
```bash
./gradlew generateStringMocks
```

### Option 2: Direct Script
```bash
./scripts/generate-string-mocks.sh
```

### Option 3: Kotlin Script
```bash
kotlin scripts/generate-string-mocks.kt
```

## 🔧 How It Works

1. **Parses XML Files**: Reads `strings.xml` and `non_translatable.xml` from `base-resources/src/main/res/values/`
2. **Extracts Properties**: Reads all `val propertyName = R.string.string_name` from `StringsIds.kt`
3. **Matches Values**: Finds the corresponding string values from XML files
4. **Generates Mocks**: Creates `application.getString()` mock statements with actual values

## 🚀 Automatic Updates

The script **automatically updates** your `BaseTest.kt` file! No more copy-pasting needed.

### What it does:
1. **🔍 Parses** your XML files and `StringsIds.kt`
2. **📝 Generates** all mock statements with actual string values
3. **🔧 Updates** `BaseTest.kt` directly with the new mocks
4. **📁 Creates** a backup file (`.backup`) before making changes
5. **✅ Done!** Your tests are ready to use

### Example of what gets updated:
```kotlin
private fun mockApplicationStrings() {
    every { application.getString(StringsIds.addShot) } returns "Add Shot"
    every { application.getString(StringsIds.login) } returns "Login"
    every { application.getString(StringsIds.settings) } returns "Settings"
    // ... all 350+ strings automatically updated!
}
```

## ⚠️ Missing Strings

If a string is not found in the XML files, the script will generate:
```kotlin
every { application.getString(StringsIds.missingString) } returns "MISSING: string_name"
```

## 🎯 Super Simple Workflow

1. ✅ Add new strings to `strings.xml` or `non_translatable.xml`
2. ✅ Add corresponding properties to `StringsIds.kt`
3. ✅ Run `./gradlew generateStringMocks`
4. ✅ **That's it!** Your `BaseTest.kt` is automatically updated! 🎉

## 🛡️ Safety Features

- **📁 Automatic Backup**: Creates `.backup` file before making changes
- **🔄 Rollback**: Restores backup if update fails
- **⚠️ Validation**: Checks for missing strings and reports them
- **🎯 Precise**: Only updates the `mockApplicationStrings()` method

## 📁 Files

- `generate-string-mocks.sh` - Main shell script
- `generate-string-mocks.kt` - Kotlin script version
- `generateMocks.gradle` - Gradle task definition
- `README.md` - This documentation
