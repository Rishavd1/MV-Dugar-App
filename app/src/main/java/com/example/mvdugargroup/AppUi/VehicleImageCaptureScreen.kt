package com.example.mvdugargroup.AppUi

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mvdugargroup.ui.theme.MVDugarGroupTheme
import java.io.File
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.mvdugargroup.PermissionDeniedDialog
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.mvdugargroup.Api.FuelIssueRequest
import com.example.mvdugargroup.Route
import com.example.mvdugargroup.viewmodel.SharedViewModel


@Composable
fun VehicleImageCaptureScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel = viewModel()
) {
    val context = LocalContext.current

    val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        Manifest.permission.READ_MEDIA_IMAGES
    else
        Manifest.permission.READ_EXTERNAL_STORAGE

    val requiredPermissions = listOf(
        Manifest.permission.CAMERA,
        storagePermission
    )

    var permissionGranted by remember { mutableStateOf(false) }
    var showPermissionDialog by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        permissionGranted = allGranted
        showPermissionDialog = !allGranted
    }

    LaunchedEffect(Unit) {
        val allGranted = requiredPermissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
        if (allGranted) {
            permissionGranted = true
        } else {
            permissionLauncher.launch(requiredPermissions.toTypedArray())
        }
    }

    if (!permissionGranted && showPermissionDialog) {
        PermissionDeniedDialog(
            onDismiss = { showPermissionDialog = false },
            onSettingsClick = {
                showPermissionDialog = false
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            }
        )
        return
    }

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val confirmedImageUri = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<String>("confirmedImageUri")
        ?.observeAsState()

    LaunchedEffect(confirmedImageUri?.value) {
        confirmedImageUri?.value?.let {
            imageUri = Uri.parse(it)
        }
    }

    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }
    val launcherCamera =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                cameraImageUri.value?.let { uri ->
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "imageUri",
                        uri.toString()
                    )
                    navController.navigate("previewImage")
                }
            }
        }

    val launcherGallery =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                imageUri = it
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    "imageUri",
                    it.toString()
                )
                navController.navigate("previewImage")
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.navigate(Route.VEHICLE_ALLOCATION) }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Capture Vehicle Meter Image",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .weight(1f)
            )
        }
        /*Text(
            "Capture Vehicle Meter Image",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 30.dp)
        )*/

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .border(1.dp, Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            imageUri?.let { uri ->
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(uri)
                        .crossfade(true)
                        .build()
                )

                Image(
                    painter = painter,
                    contentDescription = "Vehicle Image",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxSize()
                )

            } ?: run {
                Text("No Image Captured", color = Color.Gray)
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    val uri = createImageUri(context)
                    cameraImageUri.value = uri
                    launcherCamera.launch(uri)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)

            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Capture Image")
            }

            Button(
                onClick = { launcherGallery.launch("image/*") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Icon(Icons.Default.Image, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Choose from Gallery")
            }
            val TAG = "VehicleImageCaptureScreen"
            Button(
                onClick = {
                    sharedViewModel.updateFormData(
                        FuelIssueRequest(
                            fuelTypeId = sharedViewModel.selectedFuelTypeId.value!!,
                            fuelTypeName = sharedViewModel.selectedFuelTypeName.value!!,
                            businessUnitId = sharedViewModel.selectedBusinessUnitId.value!!,
                            businessUnitName = sharedViewModel.selectedBusinessUnitName.value!!,
                            warehouseId = sharedViewModel.selectedWarehouseId.value!!,
                            warehouseName = sharedViewModel.selectedWarehouseName.value!!,
                            stock = sharedViewModel.stock.value!!,
                            vehicleName = sharedViewModel.selectedVehicleName.value!!,
                            standardConsumption = sharedViewModel.standardConsumption.value!!,
                            previousReading = sharedViewModel.previousReading.value!!,
                            previousIssueDate = sharedViewModel.previousIssueDate.value!!,
                            meterStatus = sharedViewModel.meterStatusString.value!!,
                            currentReading = sharedViewModel.currentReading.value!!,
                            entryBy = sharedViewModel.entryBy.value,
                            issueNo = sharedViewModel.issueNo.value,
                            issueDate = sharedViewModel.issueDate.value,
                            assetId = sharedViewModel.assetId.value,
                            costCenter = sharedViewModel.costCenter.value
                        )
                    )

                    Log.d(TAG, "VehicleImageCaptureScreen: ${sharedViewModel.formState.value}")

                    sharedViewModel.submitForm()

                    /*Toast.makeText(context, "Saved Successfully!", Toast.LENGTH_SHORT).show()
                    navController.navigate(Route.FUEL_ISSUE_VIEW) {
                        popUpTo(Route.VEHICLE_ALLOCATION) { inclusive = true }
                    }*/
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Save")
            }
        }

    }
}

fun createImageUri(context: Context): Uri {
    val imageFile = File(context.cacheDir, "vehicle_img_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        imageFile
    )
}


fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri {
    val filename = "vehicle_image_${System.currentTimeMillis()}.png"
    val file = File(context.cacheDir, filename)
    file.outputStream().use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
    }
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )
}


@Preview(showBackground = true)
@Composable
fun VehicleImageCaptureScreenPreview() {
    val dummyNavController = rememberNavController()
    MVDugarGroupTheme {
        VehicleImageCaptureScreen(navController = dummyNavController)
    }

}

