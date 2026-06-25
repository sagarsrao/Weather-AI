# Weather AI 🌦️

A modern, high-performance Android application designed to showcase real-time weather forecasts and historical climate insights across all Indian States and Union Territories. Built using **Jetpack Compose**, **Kotlin Coroutines/Flows**, and **Retrofit**, the app retrieves high-accuracy, real-time meteorological data from the **Open-Meteo API**.

---

## 🚀 Key Features

- **State-Wise Weather Dashboard**: An interactive, visually rich grid displaying card views of Indian States and Union Territories with curated imagery.
- **Real-Time Weather Metrics**: Accurate current temperature, relative humidity, wind speeds, and descriptive weather conditions (e.g., Clear sky, Drizzle, Snowy).
- **24-Hour Hourly Forecast**: A horizontal scroll view detailing temperature variations and expected conditions hour-by-hour.
- **7-Day Daily Forecast**: Extended daily maximum/minimum temperature ranges and weather summaries.
- **Clean Architecture & MVVM**: Built with strict separation of concerns utilizing the Model-View-ViewModel design pattern.

---

## 🛠️ Tech Stack & Libraries

- **Language**: 100% [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material Design 3)
- **Asynchronous Programming**: Kotlin Coroutines & `StateFlow`
- **Networking**: [Retrofit 2](https://square.github.io/retrofit/) for REST API consumption
- **JSON Serialization**: [Moshi](https://github.com/square/moshi) with Kotlin codegen adapters
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/) for modern, asynchronous image rendering
- **Navigation**: Jetpack Compose Navigation Graph

---

## 📂 Project Structure

```directory
app/src/main/java/com/example/myapplication/
│
├── api/             # Network API Interfaces (OpenWeatherApiService.kt)
├── data/            # Data Layer Repositories (WeatherRepository.kt)
├── di/              # Dependency Injection / Service Locator (NetworkModule.kt)
├── model/           # Data Transfer Objects (DTOs) & Data Models (Weather.kt)
└── ui/              # Presentation Layer
    ├── theme/       # Design System Tokens (Colors, Typography, Themes)
    └── weather/     # UI Components & State Management (WeatherScreen, WeatherViewModel)
```

---

## 🤖 AI Pull Request Review (PR-Agent Integration)

This repository is integrated with **CodiumAI PR-Agent** via GitHub Actions to automate and enhance pull request reviews.

### Automatically Triggered Actions
- **PR Description**: Generates a clear summary, changelog, and labels for all new PRs.
- **PR Review**: Performs an automatic code analysis upon PR opening and suggests improvements.

### Interactive Slash Commands
You can interact with the PR-Agent by typing the following commands directly as **comments on any GitHub Pull Request page**:

| Slash Command | Description |
| :--- | :--- |
| `/review` | Re-runs the full AI code review and posts feedback. |
| `/describe` | Updates the PR title and description based on the code changes. |
| `/improve` | Recommends specific code quality and performance enhancements. |
| `/ask "question"` | Asks the AI a specific question about the PR's codebase. |

---

## 🛠️ Building & Running Locally

### Prerequisites
- Android Studio Ladybug (or newer)
- JDK 17
- Android SDK 24+ (Min SDK: 24, Target SDK: 36)

### Gradle Commands
To build and test the application from the command line:

- **Build Debug APK**:
  ```bash
  ./gradlew assembleDebug
  ```
- **Run Unit Tests**:
  ```bash
  ./gradlew test
  ```
- **Run Linter / Checkstyle**:
  ```bash
  ./gradlew lint
  ```
