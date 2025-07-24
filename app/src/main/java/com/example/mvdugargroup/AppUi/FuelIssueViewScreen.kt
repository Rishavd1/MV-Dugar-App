package com.example.mvdugargroup.AppUi

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mvdugargroup.dataclass.FuelIssueItem
import com.example.mvdugargroup.ui.theme.MVDugarGroupTheme
import com.example.mvdugargroup.viewmodel.SharedViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelIssueViewScreen(
    navController: NavController? = null,
    sharedViewModel: SharedViewModel = viewModel()
) {
    val scrollState = rememberScrollState()

    // Dropdown data
    val vehicleList = listOf(
        "LMVMBC001 - BOLERO CAMPER BSIII - BA",
        "LMVMBC002 - TATA ACE - BU",
        "LMVMBC003 - MAHINDRA MAXX - AM"
    )
    val businessUnits = listOf("Unit A", "Unit B", "Unit C")
    val warehouses = listOf("WH-101", "WH-102", "WH-103")
    val fuelTypes = listOf("DIESEL HIGH SPEED", "PETROL", "CNG")

    // State lifted to ViewModel (fallback to internal if needed)
    var fuelTypeExpanded by remember { mutableStateOf(false) }
    var vehicleExpanded by remember { mutableStateOf(false) }
    var businessUnitExpanded by remember { mutableStateOf(false) }
    var warehouseExpanded by remember { mutableStateOf(false) }

    var selectedFuelType by remember { mutableStateOf("Select Fuel Type") }
    var selectedVehicle by remember { mutableStateOf("") }
    var selectedBusinessUnit by remember { mutableStateOf("") }
    var selectedWarehouse by remember { mutableStateOf("") }

    var startDate by remember { mutableStateOf(LocalDate.now().minusMonths(1)) }
    var endDate by remember { mutableStateOf(LocalDate.now()) }

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    val searchResults = remember { mutableStateListOf<FuelIssueItem>() }
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Row( modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically) {

            IconButton(
                onClick = { navController?.popBackStack() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Fuel Issue View",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
//                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }


        // Fuel Type Dropdown
        DropdownField(
            label = "Fuel Type",
            value = selectedFuelType,
            expanded = fuelTypeExpanded,
            items = fuelTypes,
            onExpandedChange = { fuelTypeExpanded = !fuelTypeExpanded },
            onItemSelected = {
                selectedFuelType = it
                fuelTypeExpanded = false
            }
        )

        // Vehicle Dropdown
        DropdownField(
            label = "Select Vehicle",
            value = selectedVehicle,
            expanded = vehicleExpanded,
            items = vehicleList,
            onExpandedChange = { vehicleExpanded = !vehicleExpanded },
            onItemSelected = {
                selectedVehicle = it
                vehicleExpanded = false
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Business Unit
        DropdownField(
            label = "Select Business Unit",
            value = selectedBusinessUnit,
            expanded = businessUnitExpanded,
            items = businessUnits,
            onExpandedChange = { businessUnitExpanded = !businessUnitExpanded },
            onItemSelected = {
                selectedBusinessUnit = it
                businessUnitExpanded = false
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Warehouse
        DropdownField(
            label = "Select Warehouse",
            value = selectedWarehouse,
            expanded = warehouseExpanded,
            items = warehouses,
            onExpandedChange = { warehouseExpanded = !warehouseExpanded },
            onItemSelected = {
                selectedWarehouse = it
                warehouseExpanded = false
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Date Picker
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.weight(1f)) {
                Text("From Date", fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = startDate.format(dateFormatter),
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showStartDatePicker = true },
                    shape = RoundedCornerShape(12.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("To Date", fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = endDate.format(dateFormatter),
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showEndDatePicker = true },
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        // Search Button
        Button(
            onClick = {
                searchResults.clear()
                searchResults.addAll(mockFuelData()) // simulate
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Search, contentDescription = "Search")
            Spacer(Modifier.width(8.dp))
            Text("Search")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (searchResults.isNotEmpty()) {
            Text("Search Results", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))
            Column {
                searchResults.forEach { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text("Issue No: ${item.issueNo}", fontWeight = FontWeight.Bold)
                            Text("Fuel Type: ${item.fuelType}")
                            Text("Issue Date: ${item.issueDate.format(dateFormatter)}")
                            Text("Business Unit: ${item.businessUnit}")
                            Text("Warehouse: ${item.warehouse}")
                            Text("Vehicle: ${item.vehicleDetail}")
                        }
                    }
                }
            }
        }
    }

    // Date Pickers
    if (showStartDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showStartDatePicker = false }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(
                state = rememberDatePickerState(initialSelectedDateMillis = startDate.toEpochDay() * 86400000)
            )
        }
    }

    if (showEndDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showEndDatePicker = false }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(
                state = rememberDatePickerState(initialSelectedDateMillis = endDate.toEpochDay() * 86400000)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewFuelIssueSearchScreen() {
    val dummyNavController = rememberNavController()
    MVDugarGroupTheme {
        FuelIssueViewScreen(navController = dummyNavController)
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    label: String,
    value: String,
    expanded: Boolean,
    items: List<String>,
    onExpandedChange: () -> Unit,
    onItemSelected: (String) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange() }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            shape = RoundedCornerShape(12.dp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange() }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = { onItemSelected(item) }
                )
            }
        }
    }
}
fun mockFuelData(): List<FuelIssueItem> = listOf(
    FuelIssueItem(
        fuelType = "DIESEL HIGH SPEED",
        issueNo = "LKFISS/00010/24-25",
        issueDate = LocalDate.of(2025, 6, 13),
        businessUnit = "LAPCHE KHOLA - SUMO",
        warehouse = "Main Store Lapche Khola",
        vehicleDetail = "MAHINDRA BOLERO CAMPER"
    ),
    FuelIssueItem(
        fuelType = "DIESEL HIGH SPEED",
        issueNo = "LKFISS/00011/24-25",
        issueDate = LocalDate.of(2025, 6, 13),
        businessUnit = "LAPCHE KHOLA - SUMO",
        warehouse = "Main Store Lapche Khola",
        vehicleDetail = "TATA TIPPER 1613"
    )
    // Add more...
)
