# ClimaBit - Weather App Portfolio

A modern weather application built with Kotlin Multiplatform that works on Android, iOS, Desktop,
and Web platforms. This is a portfolio project demonstrating multiplatform development skills.

## Overview

ClimaBit is a complete weather application that provides real-time weather information, forecasts,
and air quality data. The app is built using Kotlin Multiplatform and Jetpack Compose Multiplatform,
allowing it to run on multiple platforms from a single codebase.

## Features

- Get current weather conditions including temperature, humidity, wind speed, and more
- View 7-day weather forecast with detailed predictions
- See hourly weather forecasts for the day
- Check Air Quality Index with color-coded indicators
- Search for weather in any city worldwide
- View detailed weather information with comprehensive data
- AI-powered weather insights using Google Gemini API
- Beautiful Material Design 3 interface
- Dark and light theme support
- Responsive design that works on phones, tablets, and desktops
- Shimmer loading effects for better user experience

## Technologies

- Kotlin Multiplatform
- Clean Architecture
- Jetpack Compose Multiplatform for UI development
- MVI (Model-View-Intent) architecture pattern
- Koin for Dependency Injection
- Ktor for network requests
- Google Gemini API for AI insights
- Open-Meteo API for weather data

## Libraries

- [Koin](https://insert-koin.io/) - Kotlin dependency injection library with multiplatform support
- [Ktor](https://ktor.io/docs/http-client-multiplatform.html) - Provides multiplatform libraries
  required to make network calls to REST APIs
- [ViewModel](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-viewmodel.html) -
  Compose Multiplatform ViewModel for state management
- [Coil](https://coil-kt.github.io/coil/) - Image loading library for Compose Multiplatform
- [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines) - Library support for Kotlin
  coroutines with multiplatform support
- [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) - Provides libraries for
  JSON serialization
- [kotlinx.datetime](https://github.com/Kotlin/kotlinx-datetime) - A multiplatform Kotlin library
  for working with date and time
- [Navigation Compose](https://developer.android.com/jetpack/compose/navigation) - Navigation
  component for Compose Multiplatform
- [Material 3 Adaptive](https://developer.android.com/jetpack/compose/layouts/adaptive) - Adaptive
  layouts for different screen sizes

## Getting Started

### Installation

1. Clone this repository:
   ```bash
   git clone <repository-url>
   cd ClimaBit
   ```

2. Open in the latest version of Android Studio.

3. Before running the project, obtain an API key from [Google AI](https://ai.google.dev) to
   communicate with the Gemini API.

4. Create a file `composeApp/src/commonMain/kotlin/com/talhapps/climabit/core/config/ApiKeys.kt` and
   add your API key:
   ```kotlin
   object ApiKeys {
       const val GEMINI_API_KEY = "YOUR_API_KEY"
   }
   ```

5. Run the following Gradle tasks to build the project:
    - Sync Gradle files
    - Build the project

### Run the app on your device or emulator

- For Android, run the `composeApp` module by selecting the `app` configuration. If you need help
  with the configuration, follow this link
  for [Android](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform-create-first-app.html#run-your-application-on-android)

- For iOS, run the `composeApp` module by selecting the `iosApp` configuration. If you need help
  with the configuration, follow this link
  for [iOS](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform-create-first-app.html#run-your-application-on-ios)

- For Desktop, run `./gradlew :composeApp:run`

- For Web, run `./gradlew :composeApp:wasmJsBrowserDevelopmentRun`

## Project Structure

The project follows Clean Architecture principles with clear separation of concerns:

- **data** - Data layer containing API calls and repository implementations
- **domain** - Business logic layer with models, repository interfaces, and use cases
- **presentation** - UI layer with screens, ViewModels, and components
- **core** - Shared utilities, UI components, and MVI framework
- **di** - Dependency injection modules using Koin
- **navigation** - App navigation components and utilities

## Architecture

The app uses Clean Architecture with MVI (Model-View-Intent) pattern:

- **Data Layer** - Handles all data operations including API calls to Open-Meteo and Gemini APIs
- **Domain Layer** - Contains business logic, models, and use cases
- **Presentation Layer** - Manages UI state and user interactions using MVI pattern
- **Core Layer** - Provides reusable components and utilities shared across the app

## Screenshots

The app includes the following screens:

- Dashboard Screen - Main weather overview with current conditions and hourly forecast
- Forecast Screen - 7-day weather forecast with detailed day selection
- Details Screen - Comprehensive weather details with charts and additional information
- Search Screen - Find weather for any location worldwide
- Settings Screen - App preferences and configuration

## Contributing

Feel free to contribute to this project by submitting issues, pull requests, or providing valuable
feedback. Your contributions are always welcome.

## Portfolio Highlights

This project showcases:

- Multiplatform development skills with Kotlin Multiplatform
- Clean Architecture implementation
- Modern UI development with Jetpack Compose
- API integration with RESTful services
- State management using MVI pattern
- Code organization and best practices
- Reusable component design
- Responsive and adaptive layouts

## Author

**Talha Mughal**

This is a portfolio project demonstrating multiplatform development capabilities.
