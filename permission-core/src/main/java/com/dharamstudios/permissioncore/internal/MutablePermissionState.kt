package com.dharamstudios.permissioncore.internal

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.content.ContextWrapper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.dharamstudios.permissioncore.PermissionState
import com.dharamstudios.permissioncore.PermissionStatus

internal class MutablePermissionState(
    private val permission: String,
    private val context: Context,
    private val onRationalePermission: (Boolean) -> Unit,
    private val onPermissionGranted: () -> Unit
) : PermissionState {
    override var permissionStatus: PermissionStatus by mutableStateOf(mapToPermissionStatus(permission, context))
    internal var launchAction: () -> Unit = {}
    override fun requestPermission() {
        when (permissionStatus) {
            PermissionStatus.Denied -> launchAction()
            PermissionStatus.Granted -> onPermissionGranted()
            PermissionStatus.Rationale -> onRationalePermission(true)
        }
    }

    override fun dismissRationaleRequest() {
        onRationalePermission(false)
    }
    fun refreshStatus() {
        permissionStatus = mapToPermissionStatus(permission, context)
    }
}

internal fun mapToPermissionStatus(
    permission: String,
    context: Context
): PermissionStatus {
    val isGranted =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    if (isGranted) {
        return PermissionStatus.Granted
    }
    val activity = context.findActivity() ?: return PermissionStatus.Denied
    val shouldShowRequestPermissionRationale =
        ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    return when {
        shouldShowRequestPermissionRationale -> PermissionStatus.Rationale
        else -> PermissionStatus.Denied
    }
}

internal fun Context.findActivity(): Activity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) return currentContext
        currentContext = currentContext.baseContext
    }
    return null
}