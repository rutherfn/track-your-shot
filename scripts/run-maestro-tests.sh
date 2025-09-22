#!/bin/bash

# Maestro UI Tests Runner Script
# This script runs Maestro tests locally

echo "🧪 Starting Maestro UI Tests for Track Your Shot..."

# Set up Android SDK environment
export ANDROID_HOME="/Users/nicholasrutherford/Library/Android/sdk"
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
    echo "❌ No Android device or emulator found. Please connect a device or start an emulator."
    echo "💡 Make sure your Android Studio emulator is running"
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

# Wait for app to load
echo "⏳ Waiting for app to load..."
sleep 5

# Change to project root directory
cd "$(dirname "$0")/.."

# Run the tests
echo "🧪 Running Maestro tests..."

echo "📋 Running all maestro tests..."
maestro test maestro-tests/login

echo "✅ All Maestro tests completed!"

# Optional: Show test results
echo "📊 Test results saved to maestro-tests/results/"
