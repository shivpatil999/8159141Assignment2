# NIT3213 Final Assignment — Android Application

**Student:** Shiv Bhagyesh Patil
**Student ID:** 8159141
**Campus:** Footscray

An Android app that authenticates against the `nit3213api`, lists the entities returned for
the logged-in topic, and shows full details for a selected entity. Built with Kotlin, MVVM,
Hilt, Retrofit and coroutines.

## Screens

| Screen | Purpose |
|---|---|
| **Login** | Username (student ID) and password (first name). Calls `POST /footscray/auth`, shows errors, and navigates to the Dashboard on success. |
| **Dashboard** | Calls `GET /dashboard/{keypass}` and shows the entities in a `RecyclerView`. Each row shows a summary (no description). Tapping a row opens the Details screen. |
| **Details** | Shows every field of the selected entity, including the full description. |

## Architecture

The project follows an MVVM structure with a clear separation of layers:

```
ui/            Activities + ViewModels (one package per screen)
data/model     Data classes (Entity is Parcelable for passing to Details)
data/remote    Retrofit ApiService
data/repository Repositories (interface + implementation) with error handling
di/            Hilt modules (NetworkModule, RepositoryModule)
util/          Result<T> wrapper
```

- **Dependency injection:** Hilt. `NetworkModule` provides OkHttp, Retrofit and the
  `ApiService`; `RepositoryModule` binds the repository interfaces to their implementations.
  ViewModels are injected with `@HiltViewModel`.
- **Networking:** Retrofit + Gson, with an OkHttp logging interceptor (debug builds only).
- **Async:** Kotlin coroutines. ViewModels expose a `StateFlow` of a sealed UI-state class,
  which the Activities collect with `repeatOnLifecycle`.
- **Error handling:** repositories map HTTP codes to friendly messages (e.g. 400 → wrong
  credentials, 404 → user not found) and catch `IOException` for no-network.

## How to build and run

1. **Requirements:** Android Studio (Ladybug or newer) with JDK 17, and an emulator or
   device running Android 8.0 (API 26) or higher.
2. Clone the repository:
   ```
   git clone <repository-url>
   ```
3. In Android Studio choose **File → Open** and select the `8159141Assignment2` folder.
4. Let Gradle sync (it downloads the Android Gradle Plugin, Hilt, Retrofit, etc. on first run).
5. Press **Run ▶** with an emulator or device selected.

## Logging in

- **Username:** `8159141` (student ID without the `s`).
- **Password:** your first name, exactly as registered (case-sensitive).

On success the API returns a `keypass` (the topic name), which is used for the dashboard call.

## Configuration

The base URL and campus are defined once as `BuildConfig` fields in `app/build.gradle.kts`:

```kotlin
buildConfigField("String", "BASE_URL", "\"https://nit3213apinew.onrender.com/\"")
buildConfigField("String", "CAMPUS", "\"footscray\"")
```

Change `CAMPUS` to `sydney` or `br` for a different class location; nothing else needs editing.

> Note: the API is hosted on a free tier that sleeps when idle, so the **first** login after a
> while can take up to a minute while the server wakes up.

## Unit tests

Tests live in `app/src/test/` and cover the critical components:

- `LoginViewModelTest` — validation, success and error states.
- `DashboardViewModelTest` — loading entities and error handling.
- `AuthRepositoryTest` — mapping of HTTP 200 / 400 / 404 responses.
- `EntityTest` — the summary fields shown in the list.

A `MainDispatcherRule` swaps `Dispatchers.Main` for a test dispatcher, and fake repositories
keep the tests fast and offline.

Run them with:

```
./gradlew test
```

or in Android Studio: right-click the `app/src/test` folder → **Run Tests**.

## Tech stack

Kotlin · MVVM · Hilt · Retrofit + Gson · OkHttp · Coroutines + StateFlow · Material 3 ·
RecyclerView · View Binding · JUnit4 + coroutines-test.
