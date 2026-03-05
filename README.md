# SwimScape

A Kotlin Android app for browsing swim spots, viewing safety info, favouriting spots, and viewing alerts.

## Setup

1. **Android SDK**: Ensure `ANDROID_HOME` is set or add `sdk.dir` to `local.properties`:
   ```
   sdk.dir=C\:\\Users\\YourName\\AppData\\Local\\Android\\Sdk
   ```

2. **Firebase**: Replace `app/google-services.json` with your Firebase project config:
   - Create a project at [Firebase Console](https://console.firebase.google.com)
   - Add an Android app with package `com.swimscape`
   - Download `google-services.json` and replace the placeholder

3. **Firestore structure**:
   - Collection `spots` (docId = spotId): `name`, `county`, `lat`, `lng`, `notes`, `safetySnapshot: { waterTempC, riskStatus, lastUpdated }`
   - Collection `alerts` (docId auto): `spotId`, `title`, `message`, `severity`, `timestamp`

4. **Build**: `./gradlew assembleDebug`

## Architecture

- **UI** (Compose screens) → **ViewModel** → **Repository** → **Data sources** (Firestore + Room)
- Room caches spots, favourites, and alerts (survives restarts)
- Firebase = source of truth; Room = local cache
- All DB/network on `Dispatchers.IO` via coroutines

## Screens

- Login, Register, Spots, Spot Details (with spotId arg), Favourites, Alerts, Account
- LazyColumn lists update when state changes (favourites, alerts)
