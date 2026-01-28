# 🌤️ ClimaBit - Weather App Portfolio

A beautiful, modern weather application built with **Kotlin Multiplatform** that works on **Android,
iOS, Desktop, and Web** - all from one codebase!

## 📱 What is ClimaBit?

ClimaBit is a complete weather application that shows you:

- **Current Weather** - Temperature, humidity, wind speed, and more
- **7-Day Forecast** - See what the weather will be like for the week
- **Hourly Forecast** - Hour-by-hour weather predictions
- **Air Quality Index (AQI)** - Know the air quality in your area
- **Location Search** - Find weather for any city worldwide
- **Beautiful UI** - Modern Material Design 3 interface

## 🎯 Key Features

### ✨ User Features

- **Real-time Weather Data** - Get current weather conditions instantly
- **Multi-day Forecast** - Plan ahead with 7-day weather predictions
- **Air Quality Monitoring** - Check AQI with color-coded indicators
- **Location Search** - Search and save multiple locations
- **Pull to Refresh** - Easy data refresh with a simple swipe
- **Dark/Light Theme** - Beautiful themes that adapt to your preference
- **Responsive Design** - Works perfectly on phones, tablets, and desktops

### 🏗️ Technical Features

- **Multiplatform** - One codebase for Android, iOS, Desktop, and Web
- **Clean Architecture** - Well-organized code following best practices
- **MVI Pattern** - Modern state management for predictable UI
- **Dependency Injection** - Using Koin for clean dependency management
- **Reusable Components** - Shared UI components across all screens
- **Type-Safe Navigation** - Safe navigation between screens

## 🏛️ Project Architecture

This project follows **Clean Architecture** principles with clear separation of concerns:

```
📦 ClimaBit
├── 📁 data/              → Data layer (API calls, repositories)
│   ├── remote/          → API interfaces and implementations
│   ├── repository/      → Repository implementations
│   └── core/            → Data utilities
│
├── 📁 domain/            → Business logic layer
│   ├── model/           → Data models (Weather, AQI, etc.)
│   ├── repository/      → Repository interfaces
│   └── usecase/         → Business use cases
│
├── 📁 presentation/      → UI layer (Screens and ViewModels)
│   ├── dashboard/       → Main weather screen
│   ├── forecast/        → 7-day forecast screen
│   ├── details/         → Detailed weather view
│   ├── search/          → Location search screen
│   └── settings/        → App settings
│
├── 📁 core/              → Shared core utilities
│   ├── ui/              → UI components and MVI framework
│   │   ├── components/  → Reusable UI components
│   │   │   ├── weather/ → Weather-related components
│   │   │   ├── card/    → Card components
│   │   │   ├── aqi/     → Air quality components
│   │   │   └── util/    → Utility components
│   │   └── mvi/         → MVI architecture framework
│   ├── config/          → Configuration (API keys)
│   └── domain/          → Core domain interfaces
│
├── 📁 di/                → Dependency Injection modules
│   ├── module/          → Koin modules
│   └── init/            → Koin initialization
│
└── 📁 navigation/        → App navigation
```

## 🛠️ Technologies Used

### Core Technologies

- **Kotlin Multiplatform** - Write once, run everywhere
- **Jetpack Compose Multiplatform** - Modern declarative UI
- **Ktor** - HTTP client for API calls
- **Kotlinx Serialization** - JSON parsing
- **Koin** - Dependency injection
- **Coroutines & Flow** - Asynchronous programming

### Architecture Patterns

- **MVI (Model-View-Intent)** - State management pattern
- **Clean Architecture** - Separation of concerns
- **Repository Pattern** - Data abstraction
- **Use Case Pattern** - Business logic encapsulation

### APIs Used

- **Open-Meteo API** - Weather data
- **Google Gemini API** - AI-powered weather insights

## 📂 Project Structure Explained

### Data Layer (`data/`)

Handles all data operations:

- **WeatherApi** - Fetches weather data from Open-Meteo
- **GeminiApi** - Gets AI insights from Google Gemini
- **Repositories** - Implement business logic for data fetching

### Domain Layer (`domain/`)

Contains business logic:

- **Models** - Data structures (Weather, AQI, Location)
- **Repository Interfaces** - Contracts for data operations
- **Use Cases** - Business operations (GetWeather, GetForecast, etc.)

### Presentation Layer (`presentation/`)

All UI components:

- **Screens** - Main app screens (Dashboard, Forecast, Details, Search)
- **ViewModels** - Manage screen state and business logic
- **Components** - Reusable UI pieces

### Core Layer (`core/`)

Shared utilities and frameworks:

- **MVI Framework** - Custom MVI implementation for state management
- **UI Components** - Reusable components used across screens
- **Utilities** - Helper functions (time formatting, weather icons, etc.)

## 🚀 Getting Started

### Prerequisites

- **Android Studio** (Hedgehog or newer)
- **JDK 11** or higher
- **Xcode** (for iOS development - macOS only)
- **Node.js** (for web development)

### Setup Steps

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd ClimaBit
   ```

2. **Add API Keys**
    - Create a file `composeApp/src/commonMain/kotlin/com/talhapps/climabit/core/config/ApiKeys.kt`
    - Add your API keys:
      ```kotlin
      object ApiKeys {
          const val GEMINI_API_KEY = "your-gemini-api-key"
      }
      ```

3. **Build the project**
   ```bash
   ./gradlew build
   ```

### Running on Different Platforms

#### 📱 Android

```bash
./gradlew :composeApp:assembleDebug
```

Or use Android Studio's run button.

#### 🖥️ Desktop (JVM)

```bash
./gradlew :composeApp:run
```

#### 🌐 Web (Wasm - Recommended)

```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

#### 🌐 Web (JS - Older browsers)

```bash
./gradlew :composeApp:jsBrowserDevelopmentRun
```

#### 🍎 iOS

1. Open `iosApp/iosApp.xcodeproj` in Xcode
2. Select your target device
3. Click Run

## 📸 Screenshots

The app includes:

- **Dashboard Screen** - Main weather overview with current conditions
- **Forecast Screen** - 7-day weather forecast with day selection
- **Details Screen** - Comprehensive weather details with charts
- **Search Screen** - Find weather for any location
- **Settings Screen** - App preferences

## 🎨 Design Highlights

- **Material Design 3** - Modern, beautiful UI
- **Custom Theme** - High-contrast color schemes
- **Poppins Font** - Clean, readable typography
- **Adaptive Layout** - Responsive design for all screen sizes
- **Smooth Animations** - Polished user experience

## 🏗️ Architecture Highlights

### MVI Pattern

The app uses a custom MVI (Model-View-Intent) implementation:

- **State** - Represents UI state
- **Intent** - User actions
- **Effect** - Side effects (navigation, errors)
- **Reducer** - Pure functions for state updates

### Component Organization

- **Common Components** - Shared across all screens (cards, utilities)
- **Screen Components** - Specific to each screen
- **Reusable Utilities** - Weather icons, descriptions, time formatting

### Clean Code Principles

- ✅ No code comments (self-documenting code)
- ✅ Single Responsibility Principle
- ✅ Dependency Inversion
- ✅ Separation of Concerns
- ✅ Type Safety

## 📊 Code Statistics

- **Total Kotlin Files**: 66+
- **Screens**: 5 main screens
- **ViewModels**: 5 ViewModels
- **Use Cases**: 6 use cases
- **API Interfaces**: 2 (Weather, Gemini)
- **Reusable Components**: 10+ shared components
- **Platforms Supported**: 4 (Android, iOS, Desktop, Web)

## 🔧 Development

### Project Structure Best Practices

- **Data Layer** - All API calls and data sources
- **Domain Layer** - Business logic and models
- **Presentation Layer** - UI and user interaction
- **Core Layer** - Shared utilities and frameworks

### Adding New Features

1. Create data models in `domain/model/`
2. Add API calls in `data/remote/`
3. Create repository in `data/repository/`
4. Add use case in `domain/usecase/`
5. Create ViewModel in `presentation/[screen]/`
6. Build UI in screen file

## 📝 License

This project is a portfolio piece demonstrating:

- Kotlin Multiplatform development
- Clean Architecture principles
- Modern Android/iOS development
- UI/UX design skills
- API integration
- State management patterns

## 👨‍💻 Portfolio Highlights

This project showcases:

- ✅ **Multiplatform Development** - One codebase, multiple platforms
- ✅ **Modern Architecture** - Clean Architecture with MVI pattern
- ✅ **UI/UX Design** - Beautiful, responsive Material Design 3
- ✅ **API Integration** - RESTful API consumption
- ✅ **State Management** - Custom MVI framework implementation
- ✅ **Code Organization** - Well-structured, maintainable codebase
- ✅ **Best Practices** - Following Kotlin and Android best practices
- ✅ **Reusability** - Shared components and utilities

## 🤝 Contributing

This is a portfolio project. Feel free to:

- Fork the repository
- Study the code structure
- Use as a reference for your own projects
- Suggest improvements

## 📧 Contact

For questions or feedback about this portfolio project, feel free to reach out!

---

**Built with ❤️ using Kotlin Multiplatform and Jetpack Compose**
