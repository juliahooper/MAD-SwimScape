# SwimScape

A Kotlin Android app for browsing swim spots, viewing safety info, favouriting spots, and viewing alerts.

---

## Getting Started: Full Setup Guide

### Step 1: Install Android Studio (if you haven't already)

1. Download Android Studio from [developer.android.com/studio](https://developer.android.com/studio)
2. Run the installer and follow the wizard
3. When prompted, install the **Android SDK** (this is required)
4. The SDK is usually installed at:
   - **Windows**: `C:\Users\<YourUsername>\AppData\Local\Android\Sdk`
   - **macOS**: `~/Library/Android/sdk`
   - **Linux**: `~/Android/Sdk`

### Step 2: Open the Project in Android Studio

1. Launch Android Studio
2. Click **File → Open** (or **Open** on the welcome screen)
3. Navigate to the `MAD-SwimScape` folder and select it
4. Click **OK**
5. Android Studio will detect the Gradle project and start syncing
6. Wait for the sync to finish (progress bar at the bottom). If prompted, accept any SDK or plugin updates

**How do I know if Gradle is running?**
- Look at the **bottom** of the Android Studio window. You’ll see a status bar.
- While syncing: it shows **"Gradle sync in progress..."** or a spinning icon.
- When done: it shows **"Gradle sync finished"** or **"BUILD SUCCESSFUL"**.
- If it fails: you’ll see an error message and a red **"Sync failed"**.
- To sync again: **File → Sync Project with Gradle Files** (or the elephant-with-arrow icon in the toolbar).

**Where is the src folder? I can’t see it.**
- The `src` folder is inside the **app** module, not at the project root.
- In the left **Project** panel, expand: **app** → **src** → **main** → **java** → **com** → **SwimScape**.
- If you see **"Android"** at the top of the panel, switch to **"Project"** (dropdown) to see the full folder structure.
- Full path on disk: `MAD-SwimScape/app/src/main/java/com/SwimScape/`

### Step 3: Configure the Android SDK Path

**This is not a command.** You do not type this in a terminal. You put it inside a file.

If Gradle reports "SDK location not found":

1. **Locate the file**  
   In your project folder `MAD-SwimScape`, look for a file named `local.properties` (same folder as `build.gradle.kts` and `settings.gradle.kts`).

2. **Create or edit the file**  
   - If `local.properties` does not exist: create a new text file named `local.properties` in the project root  
   - If it exists: open it in a text editor (e.g. Notepad, VS Code, or Android Studio)

3. **Add this line**  
   Put exactly one of these lines in the file (replace `YourUsername` with your Windows username):

   **On Windows:**
   ```
   sdk.dir=C\:\\Users\\YourUsername\\AppData\\Local\\Android\\Sdk
   ```
   Example: if your username is `Rosie`, it would be:
   ```
   sdk.dir=C\:\\Users\\Rosie\\AppData\\Local\\Android\\Sdk
   ```
   (The double backslashes `\\` are required.)

   **On macOS/Linux:**
   ```
   sdk.dir=/Users/YourUsername/Library/Android/sdk
   ```

4. **Find your SDK path** (if unsure)  
   In Android Studio: **File → Settings** → **Languages & Frameworks → Android SDK**. The path is shown at the top. Copy it and use it in `local.properties`.

5. **Save the file** and let Gradle sync run again.

### Step 4: Set Up Firebase

1. Go to [Firebase Console](https://console.firebase.google.com)
2. Sign in with a Google account
3. Click **Create a project** (or use an existing one)
4. Follow the wizard (you can disable Google Analytics if you prefer)
5. Once the project is created, click **Add app** → choose the **Android** icon
6. Register the app:
   - **Android package name**: `com.swimscape` (must match exactly)
   - App nickname: optional (e.g. "SwimScape")
   - Click **Register app**
7. Download the `google-services.json` file
8. Copy `app/google-services.json.example` to `app/google-services.json` and fill in your values from the Firebase Console download. (Or place the downloaded file directly in `app/`.)  
   **Important:** `google-services.json` is in `.gitignore`—never commit it. It contains API keys.
9. Continue through the Firebase setup (you can skip adding the SDK manually—it's already in the project)
10. Enable **Authentication**:
    - In Firebase Console, go to **Build → Authentication**
    - Click **Get started**
    - Under **Sign-in method**, enable **Email/Password**
11. Enable **Firestore Database**:
    - Go to **Build → Firestore Database**
    - Click **Create database**
    - Choose **Start in test mode** (for development) or production rules
    - Pick a region (e.g. `europe-west1`)

### Step 5: Add Firestore Data (Optional but Recommended)

The app works with sample data when Firestore is empty, but for real data:

**Spots collection** (create a collection named `spots`):

- Document ID: use a custom ID like `spot1`, `lough_neagh`, etc.
- Fields:
  - `name` (string): e.g. "Lough Neagh"
  - `county` (string): e.g. "Antrim"
  - `lat` (number): e.g. 54.6
  - `lng` (number): e.g. -6.4
  - `notes` (string, optional): e.g. "Popular swimming spot"
  - `safetySnapshot` (map):
    - `waterTempC` (number): e.g. 12.0
    - `riskStatus` (string): e.g. "Low", "Moderate", "High"
    - `lastUpdated` (timestamp): use Firestore's timestamp type

**Alerts collection** (create a collection named `alerts`):

- Document ID: auto-generated
- Fields:
  - `spotId` (string): must match a document ID in `spots`
  - `title` (string): e.g. "Water quality alert"
  - `message` (string): e.g. "Temporary advisory in effect"
  - `severity` (string): e.g. "info", "warning", "critical"
  - `timestamp` (timestamp): use Firestore's timestamp type

### Step 6: Run the App

1. Connect an Android device via USB (with USB debugging enabled) or start an emulator:
   - **Tools → Device Manager** to create/start an emulator
2. In the toolbar, select your device or emulator
3. Click the green **Run** button (or press **Shift+F10**)
4. The app will build and install

**Or build from the command line:**

```bash
# Windows (PowerShell)
.\gradlew.bat assembleDebug

# macOS/Linux
./gradlew assembleDebug
```

The APK will be at `app/build/outputs/apk/debug/app-debug.apk`

---

## If You Pushed google-services.json to GitHub

The file contains API keys. Do this:

1. **Rotate the key in Firebase** (most important):  
   Firebase Console → Project Settings → Your apps → find the Android app → you can’t delete the key, but you can restrict it. Add **Application restrictions** (e.g. restrict to your app’s package/sha1) and **API restrictions** so only needed APIs are allowed. For a key that’s fully exposed, consider creating a new Firebase project and migrating.

2. **Stop tracking the file**:  
   Run `git rm --cached app/google-services.json` then commit and push. The file is now in `.gitignore`, so it won’t be committed again. (It will still exist in old commits—if the repo is public, assume the key is compromised and restrict/rotate it.)

3. **Keep your local copy**:  
   The file stays on your machine. Only Git will stop tracking it.

## Troubleshooting

| Problem | Solution |
|--------|----------|
| "SDK location not found" | Create/edit `local.properties` with `sdk.dir` pointing to your Android SDK path |
| "Gradle sync failed" | Check File → Invalidate Caches → Invalidate and Restart. Ensure you have internet (Gradle downloads dependencies) |
| "google-services.json is missing" | The file must be at `app/google-services.json` (inside the `app` folder, same level as `build.gradle.kts`). If you opened the project from `StudioProjects`, that's a different folder—copy the file there or open the project from your main project folder instead. |
| Firebase Auth errors | Ensure Email/Password is enabled in Firebase Console → Authentication |
| App shows "Offline sample data" | Normal when Firestore is empty. Add spots via Firebase Console or use the sample data |
| Build fails with KSP/source sets error | Ensure `android.disallowKotlinSourceSets=false` is in `gradle.properties` |

---

## Architecture

- **UI** (Compose screens) → **ViewModel** → **Repository** → **Data sources** (Firestore + Room)
- Room caches spots, favourites, and alerts (survives restarts)
- Firebase = source of truth; Room = local cache
- All DB/network on `Dispatchers.IO` via coroutines

## Screens

- Login, Register, Spots, Spot Details (with spotId arg), Favourites, Alerts, Account
- LazyColumn lists update when state changes (favourites, alerts)
