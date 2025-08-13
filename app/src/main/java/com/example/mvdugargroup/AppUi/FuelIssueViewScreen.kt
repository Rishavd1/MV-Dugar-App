package com.example.mvdugargroup.AppUi

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.mvdugargroup.ui.theme.MVDugarGroupTheme
import com.example.mvdugargroup.viewmodel.SharedViewModel
import java.time.LocalDate
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import com.example.mvdugargroup.Api.FuelExistingEntry
import com.example.mvdugargroup.Api.FuelIssueRequest
import com.example.mvdugargroup.Route
import com.example.mvdugargroup.utils.DeleteConfirmationDialog
import com.example.mvdugargroup.utils.LoaderDialog
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.collections.emptyList


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelIssueViewScreen(
    navController: NavController? = null,
    sharedViewModel: SharedViewModel = viewModel()
) {

    LaunchedEffect(Unit) {
        sharedViewModel.fetchFuelTypes()
    }

    val fuelTypes by sharedViewModel.fuelTypes.observeAsState()
    val fuelTypesName = listOf("Select Fuel Type") + (fuelTypes?.map { it.itemType } ?: emptyList())

    val businessUnit by sharedViewModel.businessType.observeAsState()
    val businessUnitName =
        listOf("Select Business Unit") + (businessUnit?.map { it.businessUnitDesc } ?: emptyList())

    val warehouse by sharedViewModel.warehouse.observeAsState()
    var warehousesName =
        listOf("Select Warehouse") + (warehouse?.map { it.warehouseDesc } ?: emptyList())

    val vehicle by sharedViewModel.vehicleList.observeAsState()
    var vehicleNames = listOf("Select Vehicle") + (vehicle?.map { it.description } ?: emptyList())

    val isLoading by sharedViewModel.isLoading.collectAsState()


    val bottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    // Filter states
    var fuelTypeExpanded by remember { mutableStateOf(false) }
    var vehicleExpanded by remember { mutableStateOf(false) }
    var businessUnitExpanded by remember { mutableStateOf(false) }
    var warehouseExpanded by remember { mutableStateOf(false) }


    var selectedFuelType by remember { mutableStateOf(fuelTypesName.first()) }
    var selectedFuelTypeId by remember { mutableIntStateOf(0) }
    var selectedVehicle by remember { mutableStateOf("") }
    var selectedBusinessUnit by remember { mutableStateOf("") }
    var selectedWarehouse by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf(LocalDate.now().minusMonths(1)) }
    var endDate by remember { mutableStateOf(LocalDate.now()) }

    var itemToDelete by remember { mutableStateOf<FuelExistingEntry?>(null) }
    var selectedItem by remember { mutableStateOf<FuelExistingEntry?>(null) }

    var showFromDatePicker by remember { mutableStateOf(false) }
    var showToDatePicker by remember { mutableStateOf(false) }

    // Dropdown items
    /*val vehicleList = listOf(
        "LMVMBCA001",
        "LMVMBCA002",
        "LMVMBCA003",
        "LMVMBCA005"
    )*/

    val entries by sharedViewModel.existingFuelEntries.observeAsState()
    var entriesList = listOf(entries)


    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
//    val searchResults = remember { mutableStateListOf<FuelIssueRequest>() }

    LaunchedEffect(fuelTypesName, businessUnitName) {
        if (fuelTypesName.isNotEmpty()) {
            selectedFuelType = fuelTypesName[0]
        }

        if (businessUnitName.isNotEmpty()) {
            selectedBusinessUnit = businessUnitName[0]
        }

        if (vehicleNames.isNotEmpty()) {
            selectedVehicle = vehicleNames[0]
        }
    }

    LaunchedEffect(warehousesName) {
        if (warehousesName.isNotEmpty()) {
            selectedWarehouse = warehousesName[0]
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Fuel Issue View",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController?.popBackStack()
//                        navController?.popBackStack(Route.VEHICLE_IMAGE_CAPTURE,inclusive = false)
//                        navController?.navigate(Route.VEHICLE_IMAGE_CAPTURE)
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showBottomSheet = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController?.navigate(Route.FUEL_ISSUE)
                },
                containerColor = Color(0xFF006666),
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Fuel Issue")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (entriesList.isNotEmpty() && entriesList[0]?.tranId != null) {
                Text(
                    "Search Results",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF006666) // Teal
                )
                Spacer(Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(entriesList) { index, item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
                            elevation = CardDefaults.cardElevation(4.dp),
                            onClick = {
                                selectedItem = item
                            }
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.LocalGasStation,
                                        contentDescription = null,
                                        tint = Color(0xFF006666),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        "Issue No: ${item?.tranId}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = Color(0xFF004D4D)
                                    )

                                    Spacer(modifier = Modifier.weight(1f))
                                    IconButton(
                                        onClick = {
                                            itemToDelete = item
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = Color(0xFF006666),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }

                                Spacer(Modifier.height(8.dp))

                                InfoRow(label = "Fuel Type", value = item?.itemType ?: "")
                                InfoRow(
                                    label = "Issue Date",
                                    value = item?.issueDate.toString()/*.format(dateFormatter)*/
                                )
                                InfoRow(label = "Business Unit", value = item?.buDesc ?: "")
                                InfoRow(label = "Warehouse", value = item?.whDesc.toString() ?: "")
                                InfoRow(label = "Vehicle", value = item?.vehicleName ?: "")
                            }
                        }
                    }
                }
                if (itemToDelete != null) {
                    DeleteConfirmationDialog(
                        itemName = "Issue No: ${itemToDelete!!.tranId}",
                        onConfirm = {
//                            entriesList.remove(itemToDelete)
                            itemToDelete = null  // Close dialog
                        },
                        onDismiss = {
                            itemToDelete = null  // Close dialog without deleting
                        }
                    )
                }
                if (selectedItem != null) {
                    FuelIssueDetailDialog(
                        item = selectedItem!!,
                        onDismiss = { selectedItem = null }
                    )
                }
            } else {
                Text(
                    "No results to display",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = bottomSheetState
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text("Filters", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(16.dp))





                    DropdownField(
                        label = "Fuel Type",
                        value = selectedFuelType,
                        expanded = fuelTypeExpanded,
                        items = fuelTypesName,
                        onExpandedChange = { fuelTypeExpanded = !fuelTypeExpanded },
                        onItemSelected = {
                            selectedFuelType = it
                            fuelTypeExpanded = false

                            val ftId =
                                sharedViewModel.fuelTypes.value?.find { item -> item.itemType == selectedFuelType }?.itemId
                            val buId =
                                sharedViewModel.businessType.value?.find { item -> item.businessUnitDesc == selectedBusinessUnit }?.businessUnitId
                            if (ftId != null) {
                                sharedViewModel.fetchVehicleList(ftId)
                            }
                            if (ftId != null && buId != null) {
                                sharedViewModel.fetchWarehouse(buId, ftId)
                            }

                        }
                    )

                    DropdownField(
                        label = "Vehicle",
                        value = selectedVehicle,
                        expanded = vehicleExpanded,
                        items = vehicleNames,
                        onExpandedChange = { vehicleExpanded = !vehicleExpanded },
                        onItemSelected = {
                            selectedVehicle = it
                            vehicleExpanded = false
                        }
                    )

                    DropdownField(
                        label = "Business Unit",
                        value = selectedBusinessUnit,
                        expanded = businessUnitExpanded,
                        items = businessUnitName,
                        onExpandedChange = { businessUnitExpanded = !businessUnitExpanded },
                        onItemSelected = {
                            selectedBusinessUnit = it
                            businessUnitExpanded = false

                            val ftId =
                                sharedViewModel.fuelTypes.value?.find { item -> item.itemType == selectedFuelType }?.itemId
                            val buId =
                                sharedViewModel.businessType.value?.find { item -> item.businessUnitDesc == selectedBusinessUnit }?.businessUnitId
                            if (ftId != null && buId != null) {
                                sharedViewModel.fetchWarehouse(buId, ftId)
                            }


                        }
                    )

                    DropdownField(
                        label = "Warehouse",
                        value = selectedWarehouse,
                        expanded = warehouseExpanded,
                        items = warehousesName,
                        onExpandedChange = { warehouseExpanded = !warehouseExpanded },
                        onItemSelected = {
                            selectedWarehouse = it
                            warehouseExpanded = false
                        }
                    )

                    Spacer(Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                "From Date",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = Color(0xFF555555)
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF8F8F8))
                                    .clickable {

                                        showFromDatePicker = true
                                    }
                                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 16.dp, vertical = 14.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = startDate.format(dateFormatter),
                                        fontSize = 16.sp,
                                        color = Color(0xFF222222)
                                    )
                                    Icon(
                                        imageVector = Icons.Default.CalendarToday,
                                        contentDescription = "Pick Date",
                                        tint = Color(0xFF888888)
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.width(12.dp))

                        Column(Modifier.weight(1f)) {
                            Text(
                                "To Date",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = Color(0xFF555555)
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF8F8F8)) // Light background
                                    .clickable {

                                        showToDatePicker = true
                                    }
                                    .border(
                                        1.dp,
                                        Color(0xFFE0E0E0),
                                        RoundedCornerShape(12.dp)
                                    ) // Subtle border
                                    .padding(horizontal = 16.dp, vertical = 14.dp) // Inner padding
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = endDate.format(dateFormatter),
                                        fontSize = 16.sp,
                                        color = Color(0xFF222222)
                                    )
                                    Icon(
                                        imageVector = Icons.Default.CalendarToday,
                                        contentDescription = "Pick Date",
                                        tint = Color(0xFF888888)
                                    )
                                }
                            }
                        }

                    }
                    if (showFromDatePicker) {
                        DatePickerField(
                            label = "From Date",
                            selectedDate = startDate,
                            onDateSelected = { startDate = it },
                            onDismiss = { showFromDatePicker = false }
                        )
                    }
                    if (showToDatePicker) {
                        DatePickerField(
                            label = "To Date",
                            selectedDate = endDate,
                            onDateSelected = { endDate = it },
                            onDismiss = { showToDatePicker = false }
                        )
                    }
                    Spacer(Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = {
                                selectedFuelType = ""
                                selectedVehicle = ""
                                selectedBusinessUnit = ""
                                selectedWarehouse = ""
                                startDate = LocalDate.now().minusMonths(1)
                                endDate = LocalDate.now()
                                warehousesName = emptyList()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Reset")
                        }

                        Spacer(Modifier.width(16.dp))

                        Button(
                            onClick = {
                                showBottomSheet = false

                                sharedViewModel.fetchExistingEntries(
                                    startDate.format(dateFormatter),
                                    endDate.format(dateFormatter)
                                )
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Apply")
                        }


                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
        LoaderDialog(isShowing = isLoading)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    label: String,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let {
                    val instant = Instant.ofEpochMilli(it)
                    val newDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
                    onDateSelected(newDate)
                }
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
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

@Composable
fun InfoRow(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Text(
            text = "$label:",
            fontWeight = FontWeight.SemiBold,
            color = Color.DarkGray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 15.sp
        )
        Text(
            text = value,
            color = Color.Black,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontSize = 12.sp
        )
    }
}


@Composable
fun FuelIssueDetailDialog(item: FuelExistingEntry, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },
        title = { Text("Fuel Issue Details") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                InfoRow("Issue No", item.tranId.toString() ?: "null")
                InfoRow("Issue Date", item.issueDate ?: "null")
                InfoRow("Fuel Type", item.itemType ?: "null")
                InfoRow("Business Unit", item.buDesc ?: "null")
                InfoRow("Warehouse", item.whDesc.toString() ?: "null")
                InfoRow("Stock", item.stock.toString() ?: "null")
                InfoRow("Vehicle", item.vehicleName ?: "null")
                InfoRow("Standard Consumption", item.standard_Cons.toString() ?: "null")
                InfoRow("Previous Reading", item.prevReading.toString() ?: "null")
                InfoRow("Previous Issue Date", item.prevIssueDate ?: "null")
                InfoRow("Meter Status", item.meterStatus ?: "null")
                InfoRow("Current Reading", item.current_Reading.toString() ?: "null")
                InfoRow("Entry By", item.entryBy ?: "null")
            }
        }
    )
}

@Composable
fun FuelIssueRequestReadOnlyScreen(fuelIssueRequest: FuelIssueRequest) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        ReadOnlyNoFocusFieldVeh(label = "Issue No", value = fuelIssueRequest.issueNo)
        Spacer(modifier = Modifier.height(8.dp))

        ReadOnlyNoFocusFieldVeh(label = "Issue Date", value = fuelIssueRequest.issueDate)
        Spacer(modifier = Modifier.height(8.dp))

        ReadOnlyNoFocusFieldVeh(label = "Fuel Type", value = fuelIssueRequest.fuelTypeName)
        Spacer(modifier = Modifier.height(8.dp))

        ReadOnlyNoFocusFieldVeh(label = "Business Unit", value = fuelIssueRequest.businessUnitName)
        Spacer(modifier = Modifier.height(8.dp))

        ReadOnlyNoFocusFieldVeh(label = "Warehouse", value = fuelIssueRequest.warehouseName)
        Spacer(modifier = Modifier.height(8.dp))

        ReadOnlyNoFocusFieldVeh(label = "Stock (Litre)", value = fuelIssueRequest.stock.toString())
        Spacer(modifier = Modifier.height(8.dp))

        ReadOnlyNoFocusFieldVeh(label = "Vehicle", value = fuelIssueRequest.vehicleName)
        Spacer(modifier = Modifier.height(8.dp))

        ReadOnlyNoFocusFieldVeh(
            label = "Standard Consumption (Litre)",
            value = fuelIssueRequest.standardConsumption.toString()
        )
        Spacer(modifier = Modifier.height(8.dp))

        ReadOnlyNoFocusFieldVeh(
            label = "Previous Reading",
            value = fuelIssueRequest.previousReading.toString()
        )
        Spacer(modifier = Modifier.height(8.dp))

        ReadOnlyNoFocusFieldVeh(
            label = "Previous Issue Date",
            value = fuelIssueRequest.previousIssueDate
        )
        Spacer(modifier = Modifier.height(8.dp))

        ReadOnlyNoFocusFieldVeh(label = "Meter Status", value = fuelIssueRequest.meterStatus)
        Spacer(modifier = Modifier.height(8.dp))

        ReadOnlyNoFocusFieldVeh(
            label = "Current Reading",
            value = fuelIssueRequest.currentReading.toString()
        )
        Spacer(modifier = Modifier.height(8.dp))

        ReadOnlyNoFocusFieldVeh(label = "Entry By", value = fuelIssueRequest.entryBy)
    }
}

