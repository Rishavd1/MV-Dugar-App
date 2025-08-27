package com.example.mvdugargroup.AppUi

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mvdugargroup.R
import com.example.mvdugargroup.Route
import com.example.mvdugargroup.viewmodel.SharedViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, sharedViewModel: SharedViewModel) {
    val navigateToHome by sharedViewModel.navigateToHome.collectAsState()

    LaunchedEffect(navigateToHome) {
        delay(1000)
        if (navigateToHome) {
            navController.navigate(Route.MODULE_LIST) {
                popUpTo(Route.LOGIN) { inclusive = true }
            }
        } else {
            navController.navigate(Route.LOGIN)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.mvdugar),
            contentDescription = "App Logo",
            modifier = Modifier.size(200.dp)
        )
    }
}

