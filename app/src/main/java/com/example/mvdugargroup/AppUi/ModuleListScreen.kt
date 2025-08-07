package com.example.mvdugargroup.AppUi

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material3.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.mvdugargroup.Route
import com.example.mvdugargroup.ui.theme.MVDugarGroupTheme
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mvdugargroup.viewmodel.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
/*fun ModuleListScreen(
    navController: NavController,sharedViewModel: SharedViewModel = viewModel()
) {
    val moduleList = listOf(
        "MATERIAL" to Icons.Default.Inventory,       // Represents materials/items
        "CONSTRUCTION" to Icons.Default.Engineering, // Represents construction/technical work
        "PAYROLL" to Icons.Default.AttachMoney,      // Represents salary/money
        "FINANCIALS" to Icons.Default.AccountBalance, // Represents banking/finance
        "SETUP" to Icons.Default.Settings            // Represents configuration/setup
    )

    var selectedModule by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            // User name at the top, centered
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "SOMNATH DAS",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "MENU SECTION",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(32.dp))


            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(moduleList) { module ->
                    Box(
                        modifier = Modifier
                            .height(120.dp) // Makes the card square
                            .border(1.dp, Color.Black, shape = RoundedCornerShape(12.dp))
                            .background(
                                if (selectedModule == module.first) Color.LightGray else Color.White,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                selectedModule = module.first
                                if (module.first == "MATERIAL") {
                                    navController.navigate(Route.FUEL_ISSUE_VIEW)
                                }
                            }
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                        ) {
                            // Icon at the top
                            Icon(
                                imageVector = module.second,
                                contentDescription = "$module icon",
                                tint = Color.Gray,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(top = 12.dp),

                            )
                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = module.first,
                                style = TextStyle(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp,
                                    color = Color.Black,
                                    textAlign = TextAlign.Center
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}*/


fun ModuleListScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel = viewModel()
) {
    val allModules = listOf(
        "MATERIAL" to Icons.Default.Inventory,
        "CONSTRUCTION" to Icons.Default.Engineering,
        "PAYROLL" to Icons.Default.AttachMoney,
        "FINANCIALS" to Icons.Default.AccountBalance,
        "SETUP" to Icons.Default.Settings
    )





    var expanded by remember { mutableStateOf(false) }

    val favoriteModules by sharedViewModel.favoriteModules.collectAsState()
    val selectedModule by sharedViewModel.selectedModule.collectAsState()

    val userDetails by sharedViewModel.userDetails.collectAsState()

    LaunchedEffect(Unit) {
        sharedViewModel.loadUserDetails()
    }

    Box(
        modifier = Modifier
            .padding(10.dp,50.dp,10.dp,0.dp)
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            if (userDetails != null) {
                Text(
                    text = userDetails?.name ?: "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.Black
                )
            } else {
                CircularProgressIndicator()
            }


            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "MENU SECTION",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Spinner-style dropdown
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                            .clickable { expanded = true }
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Select Module",
                            color = if (selectedModule != null) Color.Black else Color.Gray
                        )
                    }
                    Box(
                        modifier = Modifier.wrapContentWidth(),
                        contentAlignment = Alignment.Center
                    ){
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .wrapContentWidth()
                                .background(Color.White)
                        ) {
                            allModules.forEach { module ->
                                val moduleName = module.first
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(moduleName)
                                            IconButton(
                                                onClick = {
                                                    val updatedFavorites = if (favoriteModules.contains(moduleName)) {
                                                        favoriteModules - moduleName
                                                    } else {
                                                        favoriteModules + moduleName
                                                    }
                                                    sharedViewModel.updateFavoriteModules(updatedFavorites)
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = if (favoriteModules.contains(moduleName))
                                                        Icons.Default.Favorite
                                                    else
                                                        Icons.Default.FavoriteBorder,
                                                    contentDescription = null,
                                                    tint = if (favoriteModules.contains(moduleName)) Color.Red else Color.Gray
                                                )
                                            }
                                        }
                                    },
                                    onClick = {
                                        sharedViewModel.updateSelectedModule(moduleName)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Grid of favorited modules
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(allModules.filter { it.first in favoriteModules }) { module ->
                    Box(
                        modifier = Modifier
                            .height(120.dp)
                            .border(1.dp, Color.Black, shape = RoundedCornerShape(12.dp))
                            .background( Color.White
                                /*if (selectedModule == module.first) Color.White else Color.LightGray*/,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                sharedViewModel.updateSelectedModule(module.first)
                                if (module.first == "MATERIAL") {
                                    navController.navigate(Route.FUEL_ISSUE_VIEW)
                                }else{
                                    Toast.makeText(
                                        navController.context,
                                        "No Further Process!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Icon(
                                imageVector = module.second,
                                contentDescription = "${module.first} icon",
                                tint = Color.Gray,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(top = 12.dp)
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = module.first,
                                style = TextStyle(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp,
                                    color = Color.Black,
                                    textAlign = TextAlign.Center
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}











@Preview(showBackground = true)
@Composable
fun ModuleListScreenPreview() {
    val dummyNavController = rememberNavController()
    MVDugarGroupTheme {
        ModuleListScreen(navController = dummyNavController)
    }
}
