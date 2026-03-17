# KomposePermissions

A modern, state-driven Android permission library built specifically for **Jetpack Compose**. Stop handling messy activity results and manual lifecycle checks, manage your permissions declaratively.

[![JitPack](https://jitpack.io/v/DharamStudios/kompose-permissions.svg)](https://jitpack.io/#DharamStudios/kompose-permissions)

## 🚀 Installation

### Step 1: Add JitPack to your `settings.gradle.kts`

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}
```


### Step 2: Add JitPack to your `settings.gradle.kts`

```kotlin
dependencies {
    implementation("com.github.DharamStudios:kompose-permissions:0.1.1")
}
```

## 💡Usage

### Single Permission Request

KomposePermissions provides a rememberPermissionState function that stays in sync with the Android Lifecycle.

```kotlin
val context = LocalContext.current

val permissionState = rememberPermissionState(
    permission = Manifest.permission.POST_NOTIFICATIONS,
    onPermissionGranted = {
        // Probably Use snackBar
        // Triggered automatically when granted (including from Settings!)
        println("Permission was granted!")
    }
) {
    AlertDialog(
        onDismissRequest = {
            dismissRationaleRequest()
        },
        confirmButton = {
            // Navigate to Notification Settings
            Button(
                onClick = {
                    Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).run {
                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        context.startActivity(this)
                    }
                }
            ) {
                Text("Open Settings")
            }
        },
        title = { Text("Permission Required") },
        text = { Text("We need notification access to alert you about updates.") }
    )
}

// Trigger the request via a button
Button(onClick = { permissionState.requestPermission() }) {
    Text("Request Permission")
}
```



