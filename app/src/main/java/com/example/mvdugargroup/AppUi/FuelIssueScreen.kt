package com.example.mvdugargroup.AppUi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mvdugargroup.Route
import com.example.mvdugargroup.ui.theme.MVDugarGroupTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelIssueScreen(navController: NavController? = null) {
    val fuelTypes = listOf("Diesel High Speed", "Petrol", "CNG")
    val businessUnits = listOf("Lapche Khola - Sumo", "Unit B", "Unit C")
    val warehouses = listOf("Warehouse A", "Warehouse B", "Warehouse C")

    var issueNo by remember { mutableStateOf("") }
    var selectedFuelType by remember { mutableStateOf(fuelTypes[0]) }
    var selectedBusinessUnit by remember { mutableStateOf(businessUnits[0]) }
    var selectedWarehouse by remember { mutableStateOf(warehouses[0]) }

    var fuelTypeExpanded by remember { mutableStateOf(false) }
    var businessUnitExpanded by remember { mutableStateOf(false) }
    var warehouseExpanded by remember { mutableStateOf(false) }

    var stock by remember { mutableStateOf("0.000") }
    val issueDate = "06-Jul-2025"


        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Fuel Issue Request",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 30.dp, bottom = 16.dp)
            )
            OutlinedTextField(
                value = issueNo,
                onValueChange = { issueNo = it },
                label = { Text("Issue No") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier.clickable {
                             }
                    )
                }
            )

            OutlinedTextField(
                value = issueDate,
                onValueChange = {},
                label = { Text("Issue Date") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Calendar",
                        modifier = Modifier.clickable {
                             }
                    )
                }
            )


            ExposedDropdownMenuBox(
                expanded = fuelTypeExpanded,
                onExpandedChange = { fuelTypeExpanded = !fuelTypeExpanded }
            ) {
                OutlinedTextField(
                    value = selectedFuelType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Fuel Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(fuelTypeExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(6.dp)
                )
                ExposedDropdownMenu(
                    expanded = fuelTypeExpanded,
                    onDismissRequest = { fuelTypeExpanded = false }
                ) {
                    fuelTypes.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                selectedFuelType = type
                                fuelTypeExpanded = false
                            }
                        )
                    }
                }
            }


            ExposedDropdownMenuBox(
                expanded = businessUnitExpanded,
                onExpandedChange = { businessUnitExpanded = !businessUnitExpanded }
            ) {
                OutlinedTextField(
                    value = selectedBusinessUnit,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Business Unit") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(businessUnitExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(6.dp)
                )
                ExposedDropdownMenu(
                    expanded = businessUnitExpanded,
                    onDismissRequest = { businessUnitExpanded = false }
                ) {
                    businessUnits.forEach { unit ->
                        DropdownMenuItem(
                            text = { Text(unit) },
                            onClick = {
                                selectedBusinessUnit = unit
                                businessUnitExpanded = false
                            }
                        )
                    }
                }
            }


            ExposedDropdownMenuBox(
                expanded = warehouseExpanded,
                onExpandedChange = { warehouseExpanded = !warehouseExpanded }
            ) {
                OutlinedTextField(
                    value = selectedWarehouse,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Warehouse") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(warehouseExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(6.dp)
                )
                ExposedDropdownMenu(
                    expanded = warehouseExpanded,
                    onDismissRequest = { warehouseExpanded = false }
                ) {
                    warehouses.forEach { warehouse ->
                        DropdownMenuItem(
                            text = { Text(warehouse) },
                            onClick = {
                                selectedWarehouse = warehouse
                                warehouseExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = stock,
                onValueChange = { stock = it },
                label = { Text("Stock") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp),
                singleLine = true
            )
//      Submit
            Button(
                onClick = {
                    navController?.navigate(Route.VEHICLE_ALLOCATION)
                    },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text("Next")
            }
//      Last 10 Entries
            OutlinedButton(
                onClick = {
                     },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text("Last 10 Entries")
            }
        }

}

@Preview(showBackground = true)
@Composable
fun FuelIssueScreenPreview() {
    val dummyNavController = rememberNavController()
    MVDugarGroupTheme {
        FuelIssueScreen(navController = dummyNavController)
    }
}