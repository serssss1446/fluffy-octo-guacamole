# Dolphy TV v1.2 — HTML/CSS/JS + Java Android Bridge

A native Android WebView app with an HTML/CSS/JavaScript remote interface and a Java bridge to Android ConsumerIrManager.

## Build requirements
- Android Studio or GitHub Actions with Android SDK
- JDK 17
- Android Gradle Plugin 8.7.3
- Android platform 35

## Build APK
Open this project in Android Studio, sync Gradle, then Build > Build APK(s).
Debug APK output: `app/build/outputs/apk/debug/app-debug.apk`.

## IR behavior and limitations
- Java bridge checks `ConsumerIrManager.hasIrEmitter()` and sends user-entered 32-bit NEC codes at 38 kHz.
- The brand picker is informational; it does not auto-detect TV models or supply brand-specific codes.
- Many TVs use other IR protocols, carrier frequencies, or code formats. NEC codes are not universal.
- The code placeholder is illustrative only and is not guaranteed to work.
- The phone must expose an Android Consumer IR emitter to this API. Test on the actual phone.
- Use only with TVs you own or have permission to control.
- No internet permission; codes are stored locally in WebView localStorage.
