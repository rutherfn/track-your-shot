#!/bin/bash

# Maestro UI Tests Runner Script for CI
# This script runs Maestro tests in CI environment

set -e  # Exit on any error

echo "🧪 Starting Maestro UI Tests for Track Your Shot (CI Mode)..."

# Set up Android SDK environment for CI
if [ -z "$ANDROID_HOME" ]; then
    export ANDROID_HOME="$HOME/Android/Sdk"
fi
export PATH="$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools:$PATH"

# Check if Maestro is installed
if ! command -v maestro &> /dev/null; then
    echo "❌ Maestro is not installed. Installing now..."
    curl -Ls "https://get.maestro.mobile.dev" | bash
    echo "✅ Maestro installed successfully"
    # Add Maestro to PATH for current session
    export PATH="$PATH:$HOME/.maestro/bin"
fi

# Check if device is connected
echo "📱 Checking for connected devices..."
if ! adb devices | grep -q "device$"; then
    echo "❌ No Android device or emulator found."
    echo "💡 This script is designed to run in CI with an emulator"
    exit 1
fi

echo "✅ Device found"

# Check if debug app is installed
echo "🔍 Checking if debug app is installed..."
if ! adb shell pm list packages | grep -q "com.nicholas.rutherford.track.your.shot.debug"; then
    echo "❌ Debug app not found. Building and installing..."
    ./gradlew assembleDebug
    adb install app/build/outputs/apk/debug/app-debug.apk
    echo "✅ Debug app installed"
fi

echo "✅ Debug app found"

# Start the app
echo "🚀 Starting Track Your Shot app..."
adb shell am start -n com.nicholas.rutherford.track.your.shot.debug/com.nicholas.rutherford.track.your.shot.MainActivity

# Wait for app to load and verify it started
echo "⏳ Waiting for app to load..."
sleep 10

# Verify the app is running
echo "🔍 Verifying app is running..."
if ! adb shell dumpsys activity activities | grep -q "com.nicholas.rutherford.track.your.shot.debug"; then
    echo "❌ App failed to start properly"
    echo "📱 Current running activities:"
    adb shell dumpsys activity activities | grep "mResumedActivity"
    exit 1
fi
echo "✅ App is running successfully"

# Change to project root directory
cd "$(dirname "$0")/.."

# Create results directory
mkdir -p maestro-tests/results

# Run the tests with proper error handling
echo "🧪 Running Maestro tests..."

# Initialize test result tracking
TEST_FAILED=false

# Run each test file individually to get better error reporting
for test_file in maestro-tests/login/*.yaml; do
    if [ -f "$test_file" ]; then
        echo "📋 Running test: $(basename "$test_file")"
        # Add timeout to prevent hanging (10 minutes per test)
        if ! timeout 600 maestro test "$test_file" --format junit --output "maestro-tests/results/$(basename "$test_file" .yaml)-results.xml"; then
            echo "❌ Test failed or timed out: $(basename "$test_file")"
            TEST_FAILED=true
        else
            echo "✅ Test passed: $(basename "$test_file")"
        fi
    fi
done

# Check overall test results
if [ "$TEST_FAILED" = true ]; then
    echo "❌ Some Maestro tests failed!"
    echo "📊 Test results saved to maestro-tests/results/"
    exit 1
else
    echo "✅ All Maestro tests passed!"
    echo "📊 Test results saved to maestro-tests/results/"
    exit 0
fi
