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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
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
    var selectedModule by remember { mutableStateOf<String?>(null) }

    val textMeasurer = rememberTextMeasurer()
    val longestWidth = remember {
        moduleList.maxOf {
            textMeasurer.measure(
                text = AnnotatedString(it),
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal)
            ).size.width
        }
    }

    val buttonWidth = with(LocalDensity.current) {
        (longestWidth + 100).toDp()
    }

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
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                moduleList.forEach { module ->
                    Box(
                        modifier = Modifier
                            .width(240.dp)
                            .border(1.dp, Color.Black, shape = RoundedCornerShape(6.dp))
                            .background(
                                if (selectedModule == module) Color.LightGray else Color.White,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .clickable {
                                selectedModule = module
                                if (module == "MATERIAL") {
                                    navController.navigate(Route.FUEL_ISSUE)
                                } else {
                                   // Toast.makeText(LocalContext.current, "No access permission.", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .padding(vertical = 12.dp, horizontal = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = module,
                            fontWeight = if (selectedModule == module) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }
                }
            }

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
