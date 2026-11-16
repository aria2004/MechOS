# MechOS

MechOS is a bilingual (Persian/English), offline-first Android toolbox for mechanical engineering students.

## Features
- Persian RTL and English LTR interface
- Persistent light/dark theme and language preferences
- Scientific calculator with DEG/RAD, trigonometric and hyperbolic functions, logarithms, factorial, memory and calculation history
- Engineering unit converter for length, mass, force, pressure, energy, power, speed, temperature and angle
- Course library with formulas, concepts and practice questions
- Search across courses and formula titles/equations
- Offline operation; no account or network permission required

## Build
Open in Android Studio or run `gradle assembleDebug` with JDK 17 and Android SDK 35.

GitHub Actions builds a debug APK on every push to `main` and uploads the artifact as `MechOS-debug-apk` for 30 days.

Package: `com.mechnos.app`
