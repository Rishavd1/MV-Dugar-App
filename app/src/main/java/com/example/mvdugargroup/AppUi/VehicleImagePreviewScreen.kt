package com.example.mvdugargroup.AppUi

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mvdugargroup.Route
import com.example.mvdugargroup.viewmodel.SharedViewModel
import kotlinx.coroutines.coroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleImagePreviewScreen(navController: NavController, imageUri: Uri?,sharedViewModel: SharedViewModel = viewModel()) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Preview Image") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            if (imageUri != null) {
                FloatingActionButton(onClick = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("confirmedImageUri", imageUri.toString())
                    navController.popBackStack()
                }) {
                    Icon(Icons.Default.Check, contentDescription = "Add Image")
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = null,
                    contentScale = ContentScale.Fit, // or ContentScale.Crop
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4f / 3f) // Or fillMaxHeight() for better vertical usage
                )
                /*Button(
                    onClick = {
                        navController.navigate(Route.FUEL_ISSUE_VIEW) {
                            popUpTo(Route.VEHICLE_IMAGE_CAPTURE) { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Save")
                }*/
            } else {
                Text("No Image to display")
            }
        }
    }
}



