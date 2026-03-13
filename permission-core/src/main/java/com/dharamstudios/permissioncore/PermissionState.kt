package com.dharamstudios.permissioncore

interface PermissionState {

    val permissionStatus: PermissionStatus

    fun requestPermission()

    fun dismissRationaleRequest()

}