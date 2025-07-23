package com.example.mvdugargroup

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mvdugargroup.AppUi.FuelIssueScreen
import com.example.mvdugargroup.AppUi.LoginScreen
import com.example.mvdugargroup.AppUi.ModuleListScreen
import com.example.mvdugargroup.AppUi.VehicleAllocationScreen
import com.example.mvdugargroup.AppUi.VehicleImageCaptureScreen
import com.example.mvdugargroup.AppUi.VehicleImagePreviewScreen
import com.example.mvdugargroup.ui.theme.MVDugarGroupTheme
import com.example.mvdugargroup.viewmodel.SharedViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MVDugarGroupTheme {
                val navController = rememberNavController()
                AppNavigator(navController)
            }
        }
    }
}

@Composable
fun AppNavigator(navController: NavHostController) {
    val sharedViewModel: SharedViewModel = viewModel()

    NavHost(navController = navController, startDestination = Route.LOGIN) {
        composable(Route.LOGIN) {
            LoginScreen(navController,sharedViewModel)
        }
        composable(Route.MODULE_LIST) {
            ModuleListScreen(navController,sharedViewModel)
        }
        composable(Route.FUEL_ISSUE) {
            FuelIssueScreen(navController,sharedViewModel)
        }
        composable(Route.VEHICLE_ALLOCATION) {
            VehicleAllocationScreen(navController,sharedViewModel)
        }
        composable(Route.VEHICLE_IMAGE_CAPTURE) {
            VehicleImageCaptureScreen(navController,sharedViewModel)
        }

        composable(Route.VEHICLE_IMAGE_PREVIEW) {
            val imageUriString = navController.previousBackStackEntry
                ?.savedStateHandle?.get<String>("imageUri")
            val imageUri = imageUriString?.let { Uri.parse(it) }

            VehicleImagePreviewScreen(navController = navController, imageUri = imageUri,sharedViewModel = sharedViewModel)
        }
    }
}
