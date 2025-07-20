package com.example.mvdugargroup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mvdugargroup.AppUi.FuelIssueScreen
import com.example.mvdugargroup.AppUi.LoginScreen
import com.example.mvdugargroup.AppUi.ModuleListScreen
import com.example.mvdugargroup.AppUi.VehicleAllocationScreen
import com.example.mvdugargroup.ui.theme.MVDugarGroupTheme

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
    NavHost(navController = navController, startDestination = Route.LOGIN) {
        composable(Route.LOGIN) {
            LoginScreen(navController)
        }
        composable(Route.MODULE_LIST) {
            ModuleListScreen(navController)
        }
        composable(Route.FUEL_ISSUE) {
            FuelIssueScreen(navController)
        }
        composable(Route.VEHICLE_ALLOCATION) {
            VehicleAllocationScreen(navController)
        }
    }
}