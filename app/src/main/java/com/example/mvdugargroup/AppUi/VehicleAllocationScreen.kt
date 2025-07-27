package com.example.mvdugargroup.AppUi

import androidx.compose.foundation.background
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mvdugargroup.Route
import com.example.mvdugargroup.viewmodel.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleAllocationScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val vehicleList = listOf(
        "LMVMBC001 - BOLERO CAMPER BSIII - BA 18 CHA 5928",
        "LMVMBC002 - BOLERO CAMPER BSIV - BA 18 CHA 5929",
        "LMVMBC003 - BOLERO CAMPER BSV - BA 20 CHA 9672"
    )

    var selectedVehicle by remember { mutableStateOf("") }
    var showDropdown by remember { mutableStateOf(false) }

    var standardCons by remember { mutableStateOf("0.0") }
    var prevReading by remember { mutableStateOf("100.0") } // Assume previous reading is 100
    var prevIssueDate by remember { mutableStateOf("2025-07-19") }

    val meterStatusOptions = listOf("METER WORKING", "METER NOT WORKING")
    var meterStatus by remember { mutableStateOf(meterStatusOptions[0]) }

    var currentReading by remember { mutableStateOf("") }
    var currentReadingError by remember { mutableStateOf("") }

    var issueQty by remember { mutableStateOf("0.000") }
    var standardQty by remember { mutableStateOf("0.000") }

    var remarks by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

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
                onClick = { navController.popBackStack() }
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
                value = searchText,
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

        /*ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    expanded = true
                },
                label = { Text("Select Vehicle") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                vehicleList.filter {
                    it.contains(searchText, ignoreCase = true)
                }.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            selectedVehicle = item
                            searchText = item
                            expanded = false
                        }
                    )
                }
            }
        }*/

        Spacer(modifier = Modifier.height(16.dp))


        LabelledField(label = "Standard Cons", value = standardCons, onValueChange = {
            standardCons = it
        })
        LabelledField(label = "Prev Reading", value = prevReading, onValueChange = {
            prevReading = it
        })
        LabelledField(label = "Prev Issue Date", value = prevIssueDate, onValueChange = {
            prevIssueDate = it
        })


        Text(
            "Meter Status",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )
        var statusExpanded by remember { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = meterStatus,
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
                meterStatusOptions.forEach {
                    DropdownMenuItem(onClick = {
                        meterStatus = it
                        statusExpanded = false
                        if (it == "METER NOT WORKING") {
                            currentReading = "0.0"
                        } else {
                            currentReading = ""
                        }
                    }, text = { Text(it) })
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
                if (meterStatus == "METER WORKING") {
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
            enabled = meterStatus == "METER WORKING"
        )

        LabelledField(
            label = "Standard Quantity",
            value = standardQty,
            onValueChange = {},
            enabled = false
        )

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
        Button(modifier = Modifier
            .fillMaxWidth()
            .height(52.dp), onClick = {
            navController.navigate(Route.VEHICLE_IMAGE_CAPTURE) {
                popUpTo(Route.MODULE_LIST) { inclusive = true }
            }
        }) {
            Text("NEXT")
        }
        Spacer(modifier = Modifier.height(25.dp))
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
        Spacer(modifier = Modifier.height(8.dp))
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
