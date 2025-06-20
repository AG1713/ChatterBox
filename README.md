# ChatterBox

ChatterBox is a real-time Android chat application built using Jetpack Compose, Firebase, and Supabase. It features one-on-one and group conversations, user authentication, media sharing, and push notifications using Firebase Cloud Messaging.

## Features

- Real-time chat with Firestore and Jetpack Compose
- Group chat functionality
- Google Sign-In using Firebase Authentication
- Firebase Cloud Messaging (FCM) for push notifications
- Image upload and sharing
- Supabase integration for storage
- Retrofit-based API calls for backend communication
- Coil for image loading
- Clean architecture with Koin for dependency injection

## Tech Stack

- Kotlin
- Jetpack Compose
- Firebase Authentication
- Firebase Firestore
- Firebase Cloud Messaging
- Firebase Storage
- Supabase
- Retrofit
- Koin
- Coil

## Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- Firebase project with Auth, Firestore, Storage, and Cloud Messaging enabled
- Supabase project for file storage

### Setup Instructions

1. Clone this repository
2. Open it in Android Studio
3. Add your google-services.json file to the app module
4. Add your Supabase URL and keys to local.properties or gradle.properties
5. Create a dummy backend which fetches FCM keys required for deliverig notifications (error handled to work without it if not)
6. Sync the project and run on a device or emulator

## Configuration

- Enable Google Sign-In in Firebase Authentication
- Set Firestore to Native mode
- Set up Firebase Cloud Messaging in Firebase console
- Store Supabase keys securely and do not commit them publicly
- If using Retrofit endpoints, make sure the backend server is reachable

## Screenshots

![Users](screenshots/UserChats.png)
![Groups](screenshots/Groups.png)
![Profile](screenshots/Profile.png)
![Group Chat](screenshots/GroupChat.png)
![Notification](screenshots/Notification.png)
