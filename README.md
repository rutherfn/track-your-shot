# Track Your Shot 
Track Your Shot, is a native Android mobile application that allows you to track your shooting percentage by two ways: 

1. <b><i>Manually Importing Shooting Percentage </i> </b>
2. <b><i>Tracking Your Live Shooting Percentages through Voice Commands  </i></b>

## Coding Language Used
Kotlin ([View](https://kotlinlang.org))

### Prerequisites
Be sure to run `git clone https://github.com/rutherfn/track-my-shot.git` on the given repo. From there, you will be able to check out the main branch to get the up to date version of features!

## Getting Started / Running The Project 

This project uses the latest Gradle version. Gradle will update if a new version gets released.  

To run this app, you will need to download [Android Studio](https://developer.android.com/studio). 

To run the project open the folder `code`

You will need to create a new emulator otherwise you can use your own android phone [Use Your Android Phone](https://javatutorial.net/connect-android-device-android-studio). 

From there the gradle will build the project and after completed, hit the `run` green button on top of the screen. 

The app will build and run starting from the splash screen. 

## Core Project 

- **`code`**: Includes the folder structure and initial classes to help you get started. Classes are organized into modules to maintain clarity and keep the code concise.

## Defined Modules

- **`app`**: Manages the main Dependency Injection across all modules. It also defines the NavigationComponent and the MainActivity, which serves as the app's entry point.
- **`app-center`**: Contains an instance of App Center, with a note to remove it in the future.
- **`base-resources`**: Handles all app resources, including drawables, strings, and raw JSON files.
- **`build-type`**: Provides configurations for debug, staging, and release variants of the app as needed.
- **`buildSrc`**: Centralizes the definition of project dependencies and libraries, which are then referenced in the `build.gradle.kts` files of other modules.
- **`compose`**: Contains reusable Compose components to avoid redundant definitions across the app.
- **`data`**: A collection of modules representing data structures returned from the ViewModel, Firebase, or Room database responses.
- **`data-test`**: Similar to `data`, but specifically for representing fake data structures used in testing scenarios, such as mocking ViewModel, Firebase, or Room responses.
- **`features`**: Breaks down and isolates app features into separate modules, adhering to the MVVM architecture, in collaboration with other supporting modules.
- **`helper`**: Includes extension functions and utility classes to simplify setup and implementation of functionalities.
- **`navigation`**: Defines the navigation tech stack, allowing for navigation actions that can be referenced in the `app` and `features` modules to invoke corresponding components and screens.
- **`shared-preferences`**: Manages stored preferences on the device, making them accessible for later use.

## MVVM(Model-View-ViewModel) Structure

### Structure:
- **Model**: Handles the data and business logic of the application, such as API calls or database operations.
- **ViewModel**: Acts as a bridge between the View and Model. It holds UI-related data and exposes it as `State` or `Flow` objects for the View to observe.
- **View (Compose)**: The UI layer built using Jetpack Compose. It observes the `State` from the ViewModel and reacts to changes by recomposing.

### Why is it Useful?
- **Separation of Concerns**: Keeps the UI logic separate from the business logic, making the codebase easier to manage and test.
- **Reactive UI**: Leverages Compose’s reactive nature to update the UI automatically when data changes.
- **Scalability**: Useful for modularizing the code, making it easier to extend and maintain as the app grows.

More info can be found here [MVVM](https://developer.android.com/codelabs/basic-android-kotlin-compose-viewmodel-and-state#0).

## Dependencies Being Used For Project

- **AppCompat**: Provides backward-compatible support for modern Android UI components.
- **Compose**: Library for building declarative UIs in Android.
- **Compose Lifecycle**: Integrates Jetpack Compose with lifecycle-aware components like ViewModel.
- **Core Testing**: Tools for writing and executing unit tests for Android applications.
- **Coroutines**: Kotlin library for managing asynchronous programming with structured concurrency.
- **Espresso**: UI testing framework for simulating and verifying user interactions.
- **Firebase**: Suite of tools and SDKs for backend services like authentication, database, and cloud messaging.
- **Google Truth**: Assertion library for writing cleaner and more expressive test assertions.
- **Gson**: A library for converting Java and Kotlin objects to and from JSON.
- **JUnit**: Framework for writing and running unit tests in Java and Kotlin.
- **JUnit Jupiter**: Next-generation JUnit framework for writing tests with more flexibility.
- **Koin**: Dependency injection framework for Kotlin, lightweight and easy to use.
- **Kover**: Used to manage unit test coverage across the mobile application, and indicate what as been tested vs what has not been tested. 
- **Material**: Provides Material Design components for building consistent UIs.
- **Mockk**: A mocking library for Kotlin for writing unit tests.
- **Room**: Jetpack library for local database storage with SQLite, built on top of SQLite.
- **Timber**: A logging utility for Android that simplifies and enhances the logging process.

## Live Google Play Store
Coming Soon

## Project Status
:white_check_mark: In Progress / Awaiting application first upload release review
