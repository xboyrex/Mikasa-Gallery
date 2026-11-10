#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"
chmod +x ./gradlew

GRADLE_ARGS=()
if [ -f "/data/data/com.termux/files/usr/bin/aapt2" ]; then
    echo ">>> Detected Termux environment: using system aapt2 override"
    GRADLE_ARGS+=("-Pandroid.aapt2FromMavenOverride=/data/data/com.termux/files/usr/bin/aapt2")
fi

echo ">>> Building Mikasa Gallery Debug APK (AMOLED Pitch Black & Onyx Yellow)..."
./gradlew assembleDebug "${GRADLE_ARGS[@]}"

APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
if [ ! -f "$APK_PATH" ]; then
    APK_PATH=$(find app/build/outputs/apk/debug -name "*.apk" | head -n 1)
fi

echo ">>> Built APK: $APK_PATH"
if command -v adb >/dev/null 2>&1 && adb get-state >/dev/null 2>&1; then
    echo ">>> Installing via ADB..."
    adb install -r "$APK_PATH"
    adb shell am start -n com.iris.gallery/.MainActivity
fi
