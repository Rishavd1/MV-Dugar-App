package com.example.mvdugargroup

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mvdugargroup.AppUi.FuelIssueScreen
import com.example.mvdugargroup.AppUi.FuelIssueViewScreen
import com.example.mvdugargroup.AppUi.LoginScreen
import com.example.mvdugargroup.AppUi.ModuleListScreen
import com.example.mvdugargroup.AppUi.SplashScreen
import com.example.mvdugargroup.AppUi.VehicleAllocationScreen
import com.example.mvdugargroup.AppUi.VehicleImageCaptureScreen
import com.example.mvdugargroup.AppUi.VehicleImagePreviewScreen
import com.example.mvdugargroup.ui.theme.MVDugarGroupTheme
import com.example.mvdugargroup.viewmodel.SharedViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost

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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigator(navController: NavHostController) {
    val sharedViewModel: SharedViewModel = viewModel()
    AnimatedNavHost( navController = navController,
        startDestination = Route.SPLASH_SCREEN,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(durationMillis = 300)
            ) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(durationMillis = 300)
            ) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(durationMillis = 300)
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(durationMillis = 300)
            ) + fadeOut(animationSpec = tween(300))
        }
    ){
        composable(Route.SPLASH_SCREEN) {
            SplashScreen(navController, sharedViewModel)
        }
        composable(Route.LOGIN) {
            LoginScreen(navController, sharedViewModel)
        }
        composable(Route.MODULE_LIST) {
            ModuleListScreen(navController, sharedViewModel)
        }
        composable(Route.FUEL_ISSUE) {
            FuelIssueScreen(navController, sharedViewModel)
        }
        composable(Route.VEHICLE_ALLOCATION) {
            VehicleAllocationScreen(navController, sharedViewModel)
        }
        composable(Route.VEHICLE_IMAGE_CAPTURE) {
            VehicleImageCaptureScreen(navController, sharedViewModel)
        }
        composable(Route.VEHICLE_IMAGE_PREVIEW) {
            val imageUriString = navController.previousBackStackEntry
                ?.savedStateHandle?.get<String>("imageUri")
            val imageUri = imageUriString?.let { Uri.parse(it) }

            VehicleImagePreviewScreen(
                navController = navController,
                imageUri = imageUri,
                sharedViewModel = sharedViewModel
            )
        }
        composable(Route.FUEL_ISSUE_VIEW) {
            FuelIssueViewScreen(navController, sharedViewModel)
        }
    }
    /*NavHost(navController = navController, startDestination = Route.LOGIN) {
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
    }*/
}
