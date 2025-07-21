package com.example.mvdugargroup


import android.Manifest
import android.os.Build
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text

@Composable
fun PermissionHandler(
    permissions: List<String>,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val context = LocalContext.current
    var allPermissionsGranted by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        allPermissionsGranted = result.values.all { it }
        if (allPermissionsGranted) onPermissionGranted() else onPermissionDenied()
    }

    LaunchedEffect(Unit) {
        val notGrantedPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PERMISSION_GRANTED
        }

        if (notGrantedPermissions.isEmpty()) {
            onPermissionGranted()
        } else {
            launcher.launch(notGrantedPermissions.toTypedArray())
        }
    }
}

@Composable
fun PermissionDeniedDialog(
    onDismiss: () -> Unit,
    onSettingsClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Permission Denied") },
        text = {
            Text("This feature requires access to Camera and Storage. Please grant permission from Settings.")
        },
        confirmButton = {
            Button(onClick = onSettingsClick) {
                Text("Go to Settings")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
