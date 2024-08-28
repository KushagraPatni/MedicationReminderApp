# Medication Reminder App

The Medication Reminder App is an Android application designed to help users manage their medication schedule effectively. This app allows users to set alarms for their medication, view scheduled reminders, and delete or update existing reminders using a user-friendly interface built with Jetpack Compose.

## Features

- **Add Medication Reminders:** Users can add new medication reminders with details such as medication name, dosage, and time.
- **View Reminders:** The app displays a list of all scheduled medication reminders.
- **Delete and Update Reminders:** Users can delete or update their reminders directly from the list.
- **Time Picker Integration:** Users can select the time for their medication reminders using a built-in time picker.
- **Repeat Reminders:** The app supports setting up periodic alarms for medications that need to be taken regularly.

## Tech Stack

- **Kotlin**: Programming language for Android development.
- **Jetpack Compose**: Modern toolkit for building native UI.
- **Hilt**: Dependency injection library to manage dependencies.
- **Room**: Persistence library for local database management.
- **Coroutines**: For managing background tasks.
- **Material 3**: For implementing Material Design components.

## Screenshots

Here are some screenshots of the app in action:

- **Main Screem:**
  ![Main Screen](https://github.com/KushagraPatni/MedicationReminderApp/blob/main/MainScreenScreenshot.jpg)


## Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/medication-reminder-app.git
    ```
2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Build and run the app on an emulator or physical device.

## Usage

- **Adding a Reminder**: Tap the "+" icon in the top right corner to open the bottom sheet where you can enter the medication details and set the time.
- **Deleting a Reminder**: Swipe left on a reminder or tap the trash icon to delete it.
- **Updating a Reminder**: To update a reminder, cancel the existing one and create a new reminder with the updated details.

## Code Structure

- **MainActivity**: The entry point of the app. Sets up the UI using Jetpack Compose.
- **MainScreen**: The main screen composable that displays the list of medication reminders and handles user interactions.
- **Form**: A composable used to enter medication details and set the reminder time.
- **ViewModel**: The `MainViewModel` manages the app's data and business logic, using LiveData to expose the list of reminders.
- **Database**: Room database for storing reminders locally.


