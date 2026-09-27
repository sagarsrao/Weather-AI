# Contributing to Weather-AI

Thank you for contributing to Weather-AI. This document provides the guidelines for setting up the project, making changes, running validation checks, and submitting pull requests.

Before contributing, make sure you have Android Studio with a recent stable version, JDK 17, Android SDK API 36, Git, and an Android emulator or physical Android device for instrumentation testing.

Weather-AI currently uses Kotlin 2.2.10, Android Gradle Plugin 9.1.1, Gradle 9.3.1, Compile SDK 36.1, Target SDK 36, and Minimum SDK 24. The project is built using Jetpack Compose, Kotlin Coroutines and StateFlow, Retrofit, Moshi, Coil, Navigation, Detekt, KtLint, JaCoCo, and SonarCloud.

After cloning the repository, open it in Android Studio and allow Gradle to synchronize before making changes.

Build the application using:

    ./gradlew :app:assembleDebug

Run unit tests using:

    ./gradlew :app:test

Run Detekt using:

    ./gradlew :app:detekt

Run KtLint checks using:

    ./gradlew ktlintCheck

Run the complete JaCoCo coverage validation using:

    ./gradlew :app:verifyCoverage

Run SonarCloud analysis using:

    ./gradlew sonar

A valid SONAR_TOKEN must be configured in the environment before running SonarCloud analysis locally.

All contributions should pass the project's configured quality checks before a pull request is created. At minimum, the following commands should complete successfully:

    ./gradlew :app:detekt
    ./gradlew ktlintCheck
    ./gradlew :app:test
    ./gradlew :app:verifyCoverage

The project also uses GitHub Actions for automated Android build, Android Lint, Detekt, KtLint, unit tests, instrumentation tests, JaCoCo coverage validation, and SonarCloud analysis. Pull requests should not be merged while required CI checks are failing.

Create a dedicated branch for each change. Recommended branch naming formats are:

    feature/<short-description>
    bugfix/<short-description>
    refactor/<short-description>
    test/<short-description>
    build/<short-description>
    docs/<short-description>

Examples include:

    feature/weather-details
    bugfix/api-error-handling
    test/weather-viewmodel
    build/update-gradle

Keep commits focused and descriptive. Examples include:

    Add weather repository tests
    Fix API error handling
    Update Gradle dependencies
    Improve weather screen state handling
    Add JaCoCo coverage validation

Avoid including unrelated changes in the same commit.

Pull requests should have a clear title and description explaining what was changed and why. Keep the scope focused, include tests for new or changed behavior where applicable, and ensure that all required CI, code-quality, and coverage checks pass before requesting review.

Dependencies should be kept reasonably up to date. Dependabot is configured to check Gradle dependencies and GitHub Actions dependencies on a scheduled basis. Dependency updates should be reviewed for compatibility and their impact on the build and tests before merging.

When reporting an issue, provide a clear description, steps to reproduce the problem, expected behavior, actual behavior, relevant logs or stack traces, and Android version and device information when applicable.

Please communicate respectfully and constructively when contributing to the project.
