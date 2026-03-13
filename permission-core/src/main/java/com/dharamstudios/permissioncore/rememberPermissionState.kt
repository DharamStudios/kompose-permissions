package com.dharamstudios.permissioncore

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.dharamstudios.permissioncore.internal.MutablePermissionState

@Composable
fun rememberPermissionState(
    permission: String,
    onPermissionGranted: () -> Unit,
    rationaleContent: @Composable PermissionState.() -> Unit,
): PermissionState {

    val context = LocalContext.current

    var isRationaleVisible by remember { mutableStateOf(false) }

    val permissionState = remember(permission) {
        MutablePermissionState(
            permission = permission,
            context = context,
            onRationalePermission = {
                isRationaleVisible = it
            },
            onPermissionGranted = onPermissionGranted
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            permissionState.refreshStatus()
        }
    )

    DisposableEffect(permissionState, permissionLauncher) {
        permissionState.launchAction = {
            permissionLauncher.launch(permission)
        }
        onDispose {
            permissionState.launchAction = {}
        }
    }


    if (isRationaleVisible) {
        rationaleContent(permissionState)
    }
    return permissionState
}