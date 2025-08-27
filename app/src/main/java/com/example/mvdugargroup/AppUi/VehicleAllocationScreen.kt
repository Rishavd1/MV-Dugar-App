package com.example.mvdugargroup.AppUi

import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mvdugargroup.Route
import com.example.mvdugargroup.utils.LoaderDialog
import com.example.mvdugargroup.viewmodel.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleAllocationScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel = viewModel()
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    BackHandler {

        navController.navigate(Route.FUEL_ISSUE) {
            popUpTo(Route.FUEL_ISSUE) { inclusive = true }
        }

    }

    val scrollState = rememberScrollState()
    val isLoading by sharedViewModel.isLoading.collectAsState()
    val meterStatusList by sharedViewModel.meterStatus.observeAsState(emptyList())
    val vehicleList by sharedViewModel.vehicleList.observeAsState(emptyList())
    val previousReadingsData by sharedViewModel.previousReadingsData.observeAsState()

    val vehicleNames =
        listOf("Select Vehicle") + (vehicleList?.map { it.description } ?: emptyList())

    var selectedVehicle by remember { mutableStateOf("") }
    var showDropdown by remember { mutableStateOf(false) }

    var standardCons by remember { mutableStateOf("0.0") }
    var prevReading by remember { mutableStateOf("100.0") }
    var prevIssueDate by remember { mutableStateOf("2025-07-19") }


    // var meterStatus by remember { mutableStateOf(meterStatusList[0].meterStatus) }

    var currentReading by remember { mutableStateOf("") }
    var currentReadingError by remember { mutableStateOf("") }

    var issueQty by remember { mutableStateOf("") }
    var standardQty by remember { mutableStateOf("") }

    var remarks by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    var selectedMeterStatus by remember { mutableStateOf("") }

    var generalErrorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        sharedViewModel.fetchMeterStatus()
        sharedViewModel.fetchVehicleList(sharedViewModel.selectedFuelTypeId.value!!)
    }

    LaunchedEffect(meterStatusList) {
        selectedVehicle = vehicleNames[0]

        if (meterStatusList.isNotEmpty() && selectedMeterStatus.isEmpty()) {
            selectedMeterStatus = meterStatusList[0].meterStatus

            sharedViewModel.meterStatusString.value = selectedMeterStatus

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
                onClick = {
                    navController.navigate(Route.FUEL_ISSUE) {
                        popUpTo(Route.FUEL_ISSUE) { inclusive = true }
                    }
                }
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


//        var vehicleExpanded by remember { mutableStateOf(false) }
        VehicleAutoCompleteTextView(
            vehicleNames = vehicleNames,
            selectedVehicle = selectedVehicle,
            onVehicleSelected = { selectedVehicle ->
                sharedViewModel.selectedVehicleName.value = selectedVehicle
                Log.d("TAG", "Selected vehicle: $selectedVehicle")
                val issueDate = sharedViewModel.issueDate.value
                sharedViewModel.fetchPrevReading(selectedVehicle, issueDate)

                sharedViewModel.selectedVehicleNumber.value =
                    vehicleList?.find { it.description == selectedVehicle.trim() }?.code

            }
        )


        Spacer(modifier = Modifier.height(16.dp))



        ReadOnlyNoFocusFieldVeh(
            label = "Standard Consumption",
            value = previousReadingsData?.st_AverageT ?: ""
        )
        Spacer(modifier = Modifier.height(12.dp))
        ReadOnlyNoFocusFieldVeh(
            label = "Previous Reading",
            value = previousReadingsData?.preV_READING?.toString() ?: ""
        )
        Spacer(modifier = Modifier.height(12.dp))
        ReadOnlyNoFocusFieldVeh(
            label = "Previous Issue Date",
            value = previousReadingsData?.preV_DATE ?: ""
        )
        Spacer(modifier = Modifier.height(12.dp))
        sharedViewModel.standardConsumption.value = previousReadingsData?.st_Average
        sharedViewModel.previousReading.value = previousReadingsData?.preV_READING?.toDouble()
        sharedViewModel.previousIssueDate.value = previousReadingsData?.preV_DATE
        sharedViewModel.standardConsumptionType.value = previousReadingsData?.st_AverageT?.split(" ")[1].toString()


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
        sharedViewModel.currentReading.value = currentReading.toDoubleOrNull()

        Spacer(modifier = Modifier.height(12.dp))


        val prevReadingDouble = previousReadingsData?.preV_READING?.toDouble() ?: 0.0
        val currentReadingDouble = currentReading.toDoubleOrNull()
        val standardComsumption = previousReadingsData?.st_AverageT?.trim()
        val standardComsumptionValue = previousReadingsData?.st_Average
        val standardConsumptionType = previousReadingsData?.unit?.trim()

        sharedViewModel.assetId.value = previousReadingsData?.fA_Id.toString()
        sharedViewModel.costCenter.value = previousReadingsData?.cC_Id.toString()

        val TAG = "VehicleAllocationScreen"
        Log.d(TAG, "VehicleAllocationScreen: standardComsumptionValue  $standardComsumptionValue")
        Log.d(TAG, "VehicleAllocationScreen: standardComsumption  $standardComsumption")
        LabelledField(
            label = "Current Reading",
            value = currentReading,
            onValueChange = { input ->
                val sanitized = input.replace(",", ".")
                val regex = """^\d*\.?\d{0,1}$""".toRegex()

                if (sanitized.isEmpty() || sanitized.matches(regex)) {
                    currentReading = sanitized
                    if (selectedMeterStatus.contains("METER WORKING", ignoreCase = true)) {
                        val cr = sanitized.toDoubleOrNull()
                        if (cr == null || cr <= prevReadingDouble) {
                            currentReadingError = "Must be greater than previous reading"
                            standardQty = "0.0"
                        } else {
                            val diff = when {
                                standardConsumptionType?.equals("KM",ignoreCase = true) == true ->
                                    (cr - prevReadingDouble) / (standardComsumptionValue ?: 0.0)

                                standardConsumptionType?.equals("HR",ignoreCase = true) == true ->
                                    (cr - prevReadingDouble) * (standardComsumptionValue ?: 0.0)

                                else -> 0.0
                            }
                            Log.d(TAG, "VehicleAllocationScreen: diff=$diff")
                            standardQty = String.format("%.1f", diff)
                            currentReadingError = ""
                        }
                    } else {
                        currentReadingError = ""
                        standardQty = "0.0"
                    }
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

        LabelledField(
            label = "Issue Quantity",
            value = issueQty,
            onValueChange = { input ->
                val sanitized = input.replace(",", ".")
                val regex = """^\d*\.?\d{0,3}$""".toRegex() // allow up to 3 decimal places
                if (sanitized.isEmpty() || sanitized.matches(regex)) {
                    issueQty = sanitized
                    if (sanitized.isNotEmpty()) {
                        sharedViewModel.issueQuanity.value = sanitized.toDoubleOrNull() ?: 0.0
                    }
                }

            }
        )

        val readingPercentage = previousReadingsData?.diff_Perc ?: 1  //diff_Reading

        val issueQtyDouble = issueQty.toDoubleOrNull() ?: 0.0
        val standardQtyDouble = standardQty.toDoubleOrNull() ?: 0.0

        val remarksRequired = if (standardQtyDouble > 0) {
            ((issueQtyDouble - standardQtyDouble) / standardQtyDouble) * 100 > readingPercentage
        } else false

        Column(modifier = Modifier.fillMaxWidth()) {
            val focusManager = LocalFocusManager.current
            Text(text = "Remarks", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = remarks,
                onValueChange = { remarks = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                singleLine = false,
                shape = RoundedCornerShape(12.dp),
                isError = remarksRequired && remarks.isBlank(),
                enabled = true,
                placeholder = { Text("Enter remarks if required") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
            )
            if (remarksRequired && remarks.isBlank()) {
                Text(
                    text = "Issue Qty greater than Standard Qty by $readingPercentage% and for that remarks mandatory.",
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }
        }


        Spacer(modifier = Modifier.height(8.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp), onClick = {

                generalErrorMessage = ""

                if(selectedVehicle.isEmpty()){
                    generalErrorMessage =
                        "Please select vehicle."
                    return@Button
                }
                if (selectedMeterStatus.contains("METER WORKING", ignoreCase = true)) {
                    val cr = currentReading.toDoubleOrNull()
                    if (cr == null || cr <= prevReadingDouble) {
                        generalErrorMessage =
                            "Current Reading must be greater than Previous Reading"
                        return@Button
                    }
                }


                if (remarksRequired && remarks.isBlank()) {
                    generalErrorMessage =
                        "Issue Qty greater than Standard Qty by $readingPercentage% and for that remarks mandatory."
                    return@Button
                }


                if ((issueQty.toDoubleOrNull() ?: 0.0) <= 0) {
                    generalErrorMessage = "Issue Quantity must be greater than 0"
                    return@Button
                }
                if (remarksRequired && remarks.isEmpty()) {
                    generalErrorMessage = "Remarks is mandatory in this case."
                    return@Button
                }

                navController.navigate(Route.VEHICLE_IMAGE_CAPTURE) {
                    popUpTo(Route.MODULE_LIST) { inclusive = true }
                }
            }) {
            Text("NEXT")
        }
        if (generalErrorMessage.isNotEmpty()) {
            Text(
                text = generalErrorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        LoaderDialog(isShowing = isLoading)
        Spacer(modifier = Modifier.height(35.dp))

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleAutoCompleteTextView(
    vehicleNames: List<String>,
    selectedVehicle: String,
    onVehicleSelected: (String) -> Unit
) {
    var searchText by remember { mutableStateOf(selectedVehicle) }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                expanded = true
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            trailingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Clear Button
                    if (searchText.isNotEmpty()) {
                        IconButton(onClick = {
                            searchText = ""
                            onVehicleSelected("") // reset selection
                            expanded = false
                        }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = Color.Gray
                            )
                        }
                    }

                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            },
            placeholder = { Text("Select Vehicle") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        val filteredList = vehicleNames.filter {
            it.contains(searchText, ignoreCase = true)
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (filteredList.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("No matches found") },
                    onClick = { expanded = false }
                )
            } else {
                filteredList.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            searchText = item
                            onVehicleSelected(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun ReadOnlyNoFocusFieldVeh(label: String, value: String) {
    Column {
        Text(
            text = label, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start)
        )
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
            enabled = enabled,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
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

/*@Preview(showBackground = true)
@Composable
fun VehicleAllocationScreenPreview() {
    val dummyNavController = rememberNavController()
    MVDugarGroupTheme {
        VehicleAllocationScreen(navController = dummyNavController)
    }
}*/
