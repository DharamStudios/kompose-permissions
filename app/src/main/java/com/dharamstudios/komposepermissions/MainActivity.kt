package com.dharamstudios.komposepermissions

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.dharamstudios.komposepermissions.ui.theme.KomposePermissionsTheme
import com.dharamstudios.permissioncore.PermissionState
import com.dharamstudios.permissioncore.rememberPermissionState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KomposePermissionsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val permissionState = rememberPermissionState(
                        permission = Manifest.permission.POST_NOTIFICATIONS,
                        onPermissionGranted = {
                            //Probably Use snackBar
                        }
                    ) {
                        AlertDialog(
                            onDismissRequest = {
                                dismissRationaleRequest()
                                Log.d(
                                    "PermissionState",
                                    "dismissRationaleRequest: $this"
                                )
                            },
                            confirmButton = {
                                //Navigate to Notification Settings
                                Button(
                                    onClick = {
                                        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).run {
                                            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                                            startActivity(this)
                                        }
                                    }
                                ) {
                                    Text("Open Settings")
                                }

                            },
                            text = { Text("Bro, please give camera access so we can scan.") }
                        )
                    }


                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        Button(
                            onClick = {
                                permissionState.requestPermission()
                            }
                        ) {
                            Text("Request Permission")
                        }
                        Text(permissionState.permissionStatus.toString())
                    }
                }
            }
        }
    }
}
