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

# Check if device is connected and wait for it to be ready
echo "📱 Checking for connected devices..."
max_attempts=30
attempt=0

while [ $attempt -lt $max_attempts ]; do
    if adb devices | grep -q "device$"; then
        echo "✅ Device found"
        break
    fi
    echo "⏳ Waiting for device... (attempt $((attempt + 1))/$max_attempts)"
    sleep 2
    attempt=$((attempt + 1))
done

if [ $attempt -eq $max_attempts ]; then
    echo "❌ No Android device or emulator found after $max_attempts attempts"
    echo "📱 Current device status:"
    adb devices
    exit 1
fi

# Wait for device to be fully ready
echo "⏳ Waiting for device to be fully ready..."
adb wait-for-device
adb shell 'while [[ -z $(getprop sys.boot_completed) ]]; do sleep 1; done'
echo "✅ Device is fully ready"

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

# Function to check if device is still connected
check_device_connection() {
    if ! adb devices | grep -q "device$"; then
        echo "⚠️ Device disconnected, attempting to reconnect..."
        sleep 5
        if ! adb devices | grep -q "device$"; then
            echo "❌ Device still disconnected after retry"
            return 1
        fi
        echo "✅ Device reconnected"
    fi
    return 0
}

# Run each test file individually to get better error reporting
for test_file in maestro-tests/login/*.yaml; do
    if [ -f "$test_file" ]; then
        echo "📋 Running test: $(basename "$test_file")"
        
        # Check device connection before each test
        if ! check_device_connection; then
            echo "❌ Device connection lost, skipping remaining tests"
            TEST_FAILED=true
            break
        fi
        
        # Add timeout to prevent hanging (10 minutes per test)
        if ! timeout 600 maestro test "$test_file" --format junit --output "maestro-tests/results/$(basename "$test_file" .yaml)-results.xml"; then
            echo "❌ Test failed or timed out: $(basename "$test_file")"
            TEST_FAILED=true
            
            # Check if failure was due to device disconnection
            if ! check_device_connection; then
                echo "❌ Device disconnected during test, stopping test execution"
                break
            fi
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
