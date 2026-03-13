package com.dharamstudios.permissioncore

sealed interface PermissionStatus {
    object Granted : PermissionStatus
    object Denied : PermissionStatus
    object Rationale : PermissionStatus
}