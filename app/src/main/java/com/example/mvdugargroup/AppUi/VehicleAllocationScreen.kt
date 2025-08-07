package com.example.mvdugargroup.AppUi

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mvdugargroup.ui.theme.MVDugarGroupTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mvdugargroup.Route
import com.example.mvdugargroup.utils.LoaderDialog
import com.example.mvdugargroup.viewmodel.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleAllocationScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel = viewModel()
) {


    val scrollState = rememberScrollState()
    val isLoading by sharedViewModel.isLoading.collectAsState()
    val meterStatusList by sharedViewModel.meterStatus.observeAsState(emptyList())

    val vehicleList = listOf(
        "LMVMBCA001",
        "LMVMBCA002",
        "LMVMBCA003",
        "LMVMBCA004",
        "LMVMBCA005",
    )

    var selectedVehicle by remember { mutableStateOf("") }
    var showDropdown by remember { mutableStateOf(false) }

    var standardCons by remember { mutableStateOf("0.0") }
    var prevReading by remember { mutableStateOf("100.0") }
    var prevIssueDate by remember { mutableStateOf("2025-07-19") }


    // var meterStatus by remember { mutableStateOf(meterStatusList[0].meterStatus) }

    var currentReading by remember { mutableStateOf("") }
    var currentReadingError by remember { mutableStateOf("") }

    var issueQty by remember { mutableStateOf("0.000") }
    var standardQty by remember { mutableStateOf("0.000") }

    var remarks by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    var selectedMeterStatus by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        sharedViewModel.fetchMeterStatus()
    }

    LaunchedEffect(meterStatusList) {
        selectedVehicle = vehicleList[0]

        if (meterStatusList.isNotEmpty() && selectedMeterStatus.isEmpty()) {
            selectedMeterStatus = meterStatusList[0].meterStatus
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(3.dp),
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
                onClick = { navController.navigate(Route.FUEL_ISSUE){
                    popUpTo(Route.FUEL_ISSUE) { inclusive = true }
                } }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Vehicle Allocation",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .weight(1f)
            )
        }


        var vehicleExpanded by remember { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedVehicle,
                onValueChange = {
                    searchText = it
                    vehicleExpanded = true
                },
                modifier = Modifier
                    .fillMaxWidth(),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.clickable { vehicleExpanded = true }
                    )
                },
                placeholder = { Text("Select Vehicle") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            DropdownMenu(
                expanded = vehicleExpanded,
                onDismissRequest = { vehicleExpanded = false },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                vehicleList
                    .filter { it.contains(searchText, ignoreCase = true) }
                    .forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                selectedVehicle = item
                                searchText = item
                                vehicleExpanded = false
                            }
                        )
                    }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))



        ReadOnlyNoFocusFieldVeh(label = "Standard Consumption", value = standardCons)
        Spacer(modifier = Modifier.height(12.dp))
        ReadOnlyNoFocusFieldVeh(label = "Previous Reading", value = prevReading)
        Spacer(modifier = Modifier.height(12.dp))
        ReadOnlyNoFocusFieldVeh(label = "Previous Issue Date", value = prevIssueDate)
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "Meter Status",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )
        var statusExpanded by remember { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedMeterStatus,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.clickable { statusExpanded = true }
                    )
                },
                shape = RoundedCornerShape(12.dp)
            )
            DropdownMenu(expanded = statusExpanded, onDismissRequest = { statusExpanded = false }) {
                meterStatusList.forEach {
                    DropdownMenuItem(onClick = {
                        selectedMeterStatus = it.meterStatus
                        statusExpanded = false
                        if (it.meterStatus.contains("METER NOT WORKING", ignoreCase = true)) {
                            currentReading = "0.0"
                        } else {
                            currentReading = ""
                        }
                    }, text = { Text(it.meterStatus) })
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))


        val prevReadingDouble = prevReading.toDoubleOrNull() ?: 0.0
        val currentReadingDouble = currentReading.toDoubleOrNull()

        LabelledField(
            label = "Current Reading",
            value = currentReading,
            onValueChange = {
                currentReading = it
                if (selectedMeterStatus.contains("METER WORKING", ignoreCase = true)) {
                    val cr = it.toDoubleOrNull()
                    currentReadingError = if (cr == null || cr <= prevReadingDouble) {
                        "Must be greater than previous reading"
                    } else {
                        // calculate Standard Quantity
                        val diff = cr - prevReadingDouble
                        standardQty = String.format("%.3f", diff)
                        ""
                    }
                } else {
                    currentReadingError = ""
                    standardQty = "0.000"
                }
            },
            isError = currentReadingError.isNotEmpty(),
            errorText = currentReadingError,
            enabled = selectedMeterStatus.contains("METER WORKING", ignoreCase = true)
        )

        ReadOnlyNoFocusFieldVeh(label = "Standard Quantity", value = standardQty)
        Spacer(modifier = Modifier.height(12.dp))
        /*LabelledField(
            label = "Standard Quantity",
            value = standardQty,
            onValueChange = {},
            enabled = false
        )*/

        LabelledField(label = "Issue Quantity", value = issueQty, onValueChange = {
            issueQty = it

        })
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Remarks", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = remarks,
                onValueChange = { remarks = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                isError = false,
                enabled = true
            )


        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp), onClick = {
                navController.navigate(Route.VEHICLE_IMAGE_CAPTURE) {
                    popUpTo(Route.MODULE_LIST) { inclusive = true }
                }
            }) {
            Text("NEXT")
        }
        LoaderDialog(isShowing = isLoading)
        Spacer(modifier = Modifier.height(25.dp))

    }
}


@Composable
fun ReadOnlyNoFocusFieldVeh(label: String, value: String) {
    Column {
        Text(text = label
            , fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(
                    width = 0.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text = value, color = Color.Black)
        }
    }
}

@Composable
fun LabelledField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    errorText: String = "",
    enabled: Boolean = true
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            isError = isError,
            enabled = enabled
        )
        if (isError && errorText.isNotEmpty()) {
            Text(
                text = errorText,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.Start)
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun VehicleAllocationScreenPreview() {
    val dummyNavController = rememberNavController()
    MVDugarGroupTheme {
        VehicleAllocationScreen(navController = dummyNavController)
    }
}
