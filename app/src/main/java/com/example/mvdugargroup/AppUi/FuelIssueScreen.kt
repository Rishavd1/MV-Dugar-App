package com.example.mvdugargroup.AppUi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mvdugargroup.Route
import com.example.mvdugargroup.ui.theme.MVDugarGroupTheme
import com.example.mvdugargroup.viewmodel.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelIssueScreen(navController: NavController,sharedViewModel: SharedViewModel = viewModel()) {

    LaunchedEffect(Unit) {
        sharedViewModel.fetchFuelTypes()
    }

    val fuelTypes = sharedViewModel.fuelTypes.value?.map { it.itemType }?: emptyList()
    val businessUnit = sharedViewModel.businessType.value?.map { it.businessUnitDesc }?:emptyList()

    //val businessUnits = listOf("Lapche Khola - Sumo", "Unit B", "Unit C")
    val warehouses = listOf("Warehouse A", "Warehouse B", "Warehouse C")

    var issueNo by remember { mutableStateOf("") }
    var selectedFuelType by remember { mutableStateOf("") }
    var selectedBusinessUnit by remember { mutableStateOf("") }
    var selectedWarehouse by remember { mutableStateOf(warehouses[0]) }

    var fuelTypeExpanded by remember { mutableStateOf(false) }
    var businessUnitExpanded by remember { mutableStateOf(false) }
    var warehouseExpanded by remember { mutableStateOf(false) }

    var stock by remember { mutableStateOf("0.000") }
    val issueDate = "06-Jul-2025"

    val scrollState = rememberScrollState()

    LaunchedEffect(fuelTypes) {
        if (fuelTypes.isNotEmpty()) {
            selectedFuelType = fuelTypes[0]
        }
        if (businessUnit.isNotEmpty()){
            selectedBusinessUnit = businessUnit[0]
        }
    }

    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize()
            .verticalScroll(scrollState),
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
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Fuel Issue Request",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f)
            )
        }
        Text("Issue No", fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = issueNo,
            onValueChange = { issueNo = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Issue Date
        Text("Issue Date", fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = issueDate,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Calendar"
                )
            }
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Fuel Type Dropdown
        Text("Fuel Type", fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(4.dp))
        ExposedDropdownMenuBox(
            expanded = fuelTypeExpanded,
            onExpandedChange = { fuelTypeExpanded = !fuelTypeExpanded }
        ) {
            OutlinedTextField(
                value = selectedFuelType,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(fuelTypeExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
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
        Spacer(modifier = Modifier.height(12.dp))

        Text("Business Unit", fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(4.dp))
        ExposedDropdownMenuBox(
            expanded = businessUnitExpanded,
            onExpandedChange = { businessUnitExpanded = !businessUnitExpanded }
        ) {
            OutlinedTextField(
                value = selectedBusinessUnit,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(businessUnitExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(
                expanded = businessUnitExpanded,
                onDismissRequest = { businessUnitExpanded = false }
            ) {
                businessUnit.forEach { unit ->
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
        Spacer(modifier = Modifier.height(12.dp))

        Text("Warehouse", fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(4.dp))
        ExposedDropdownMenuBox(
            expanded = warehouseExpanded,
            onExpandedChange = { warehouseExpanded = !warehouseExpanded }
        ) {
            OutlinedTextField(
                value = selectedWarehouse,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(warehouseExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
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
        Spacer(modifier = Modifier.height(12.dp))

        // Stock
        Text("Stock", fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = stock,
            onValueChange = { stock = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                navController.navigate(Route.VEHICLE_ALLOCATION){
                    popUpTo(Route.MODULE_LIST) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Next")
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