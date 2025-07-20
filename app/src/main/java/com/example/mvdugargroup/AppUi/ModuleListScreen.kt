package com.example.mvdugargroup.AppUi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.mvdugargroup.Route
import com.example.mvdugargroup.ui.theme.MVDugarGroupTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModuleListScreen(
    navController: NavController,
    userName: String = "Somnath Das",
    userId: String = "somnathd"
) {
    val moduleList = listOf("MATERIAL", "CONSTRUCTION", "PAYROLL", "FINANCIALS", "SETUP")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("SELECT MODULE") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "MENU SECTION",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))


            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(start = 8.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "USER NAME: ",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "$userName (SCE1485)",
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))


            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(start = 8.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "USER ID: ",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = userId,
                    color = Color.DarkGray
                )
            }


            Spacer(modifier = Modifier.height(24.dp))
            ModuleDropdown(
                navController = navController,
                moduleList = moduleList,
                selectedText = selectedText,
                onModuleSelected = { selectedText = it }
            )
            /*// Dropdown-style module selector
            val scrollState = rememberScrollState()
            Box(
                modifier = Modifier
                    .width(280.dp)
                    .height(40.dp)
                    .background(Color(0xFFB0B0B0))
                    .clickable { expanded = !expanded },
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White // Pinkish-red
                    )
                    Icon(
                        imageVector = if (expanded)
                            Icons.Default.KeyboardArrowUp
                        else
                            Icons.Default.KeyboardArrowDown,
                        contentDescription = "Toggle Dropdown",
                        tint = Color.Black
                    )
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .heightIn(max = 200.dp) // Set a max height to prevent upward shift
                        .width(280.dp)
                        .border(1.dp, Color.Gray)
                        .verticalScroll(scrollState)
                        .background(Color.White)
                ){
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        moduleList.forEach { module ->
                            HorizontalDivider(
                                modifier = Modifier.width(200.dp),
                                thickness = 1.dp,
                                color = Color.LightGray
                            )
                            Text(
                                text = module,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedText = module
                                        expanded = false
                                        if (module == "MATERIAL") {
                                            navController.navigate("fuelIssue")
                                        }
                                    }
                                    .padding(vertical = 12.dp, horizontal = 24.dp),
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

            }*/
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModuleDropdown(
    navController: NavController,
    moduleList: List<String>,
    selectedText: String,
    onModuleSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.width(280.dp)
    ) {
        TextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            label = { Text("Select Module") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .menuAnchor()
                .height(56.dp)

        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            moduleList.forEach { module ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = module,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    onClick = {
                        onModuleSelected(module)
                        expanded = false
                        if (module == "MATERIAL") {
                            navController.navigate(Route.FUEL_ISSUE)
                        }
                    }
                )
            }
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
