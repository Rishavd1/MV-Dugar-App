package com.example.mvdugargroup.AppUi

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mvdugargroup.Route
import com.example.mvdugargroup.ui.theme.MVDugarGroupTheme
import com.example.mvdugargroup.utils.LoaderDialog
import com.example.mvdugargroup.viewmodel.SharedViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelIssueScreen(navController: NavController,sharedViewModel: SharedViewModel = viewModel()) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        sharedViewModel.fetchFuelTypes()
    }

    val fuelTypesName = sharedViewModel.fuelTypes.value?.map { it.itemType }?: emptyList()
    val businessUnitName = sharedViewModel.businessType.value?.map { it.businessUnitDesc }?:emptyList()
    val warehousesName = sharedViewModel.warehouse.value?.map{ it.warehouseDesc}?:emptyList()
    val stockQuantity by sharedViewModel.stockQuantity.observeAsState()


    val isLoading by sharedViewModel.isLoading.collectAsState()

    var issueNo by remember { mutableStateOf("") }
    var selectedFuelType by remember { mutableStateOf("") }
    var selectedFuelTypeId by remember { mutableStateOf("") }
    var selectedBusinessUnit by remember { mutableStateOf("") }
    var selectedWarehouse by remember { mutableStateOf("") }


    var fuelTypeExpanded by remember { mutableStateOf(false) }
    var businessUnitExpanded by remember { mutableStateOf(false) }
    var warehouseExpanded by remember { mutableStateOf(false) }

    var stock by remember { mutableStateOf("0.000") }
    val issueDate: String = LocalDate.now()
        .format(DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH))

    sharedViewModel.issueDate.value = issueDate
    val scrollState = rememberScrollState()

    val TAG = "FuelIssueScreen"

    LaunchedEffect(fuelTypesName,businessUnitName) {
        if (fuelTypesName.isNotEmpty()) {
            selectedFuelType = fuelTypesName[0]
        }
        if (businessUnitName.isNotEmpty()){
            selectedBusinessUnit = businessUnitName[1]  //TODO

            val ftId = sharedViewModel.fuelTypes.value?.find { it.itemType == selectedFuelType }?.itemId
            val buId = sharedViewModel.businessType.value?.find { it.businessUnitDesc == selectedBusinessUnit }?.businessUnitId
            if (ftId != null && buId != null) {
                sharedViewModel.fetchWarehouse(buId, ftId)
            }
        }
    }
    LaunchedEffect(warehousesName) {
        if (warehousesName.isNotEmpty() && selectedWarehouse.isEmpty()) {
            selectedWarehouse = warehousesName[0]
        }

        val ftId = sharedViewModel.fuelTypes.value
            ?.find { it.itemType == selectedFuelType }?.itemId
        val buId = sharedViewModel.businessType.value
            ?.find { it.businessUnitDesc == selectedBusinessUnit }?.businessUnitId
        val whId = sharedViewModel.warehouse.value
            ?.find { it.warehouseDesc == selectedWarehouse }?.warehouseId

        if (ftId != null && buId != null && whId != null) {
            // Fetch stock quantity
            sharedViewModel.fetchStockQuantity(buId, ftId, whId)

            // Save selections in ViewModel
            sharedViewModel.selectedFuelTypeId.value = ftId
            sharedViewModel.selectedBusinessUnitId.value = buId
            sharedViewModel.selectedWarehouseId.value = whId
            sharedViewModel.selectedFuelTypeName.value = selectedFuelType
            sharedViewModel.selectedBusinessUnitName.value = selectedBusinessUnit
            sharedViewModel.selectedWarehouseName.value = selectedWarehouse

            Log.d("TAG", "FuelIssueScreen: ftId=$ftId, buId=$buId, whId=$whId")
        }
    }


    val stockDisplay = stockQuantity?.stockQuantity?.let {
        String.format("%.3f", it)
    } ?: "0.0"

    sharedViewModel.stock.value = stockQuantity?.stockQuantity


    issueNo = "Issue 1"
    sharedViewModel.issueNo.value = issueNo
    Log.d(TAG, "FuelIssueScreen: Stock ${stockQuantity?.stockQuantity}")
    Log.d(TAG, "FuelIssueScreen: ViewModelStock ${sharedViewModel.stock.value}")

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
        Spacer(modifier = Modifier.height(4.dp))
        ReadOnlyNoFocusField("Issue No",issueNo) //issueNo
        Spacer(modifier = Modifier.height(12.dp))
        Spacer(modifier = Modifier.height(4.dp))
        ReadOnlyNoFocusField("Issue Date",issueDate)

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
                onValueChange = {

                },
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
                fuelTypesName.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            try {
                                selectedFuelType = type
                                fuelTypeExpanded = false
                                val _selectedFuelTypeId = sharedViewModel.fuelTypes.value?.find { it.itemType == selectedFuelType }?.itemId!!
                                selectedFuelTypeId = _selectedFuelTypeId.toString()
                                val selectedBusinessUnitId = sharedViewModel.businessType.value?.find { it.businessUnitDesc == selectedBusinessUnit }?.businessUnitId!!
                                Log.d("TAG", "FuelIssueScreen:selectedFuelTypeId = $_selectedFuelTypeId  selectedBusinessUnitId = $selectedBusinessUnitId ")

                                if (_selectedFuelTypeId != null && selectedBusinessUnitId != null) {
                                    sharedViewModel.fetchWarehouse(selectedBusinessUnitId, _selectedFuelTypeId)
                                }
                            }catch (e: Exception){
                                Toast.makeText(context, "Unfortunate Error Occurred!\n ${e.message}", Toast.LENGTH_SHORT).show()
                            }

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
                onValueChange = {

                },
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
                businessUnitName.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text(unit) },
                        onClick = {
                            selectedBusinessUnit = unit
                            businessUnitExpanded = false
                            val selectedFuelTypeId = sharedViewModel.fuelTypes.value?.find { it.itemType == selectedFuelType }?.itemId!!
                            val selectedBusinessUnitId = sharedViewModel.businessType.value?.find { it.businessUnitDesc == selectedBusinessUnit }?.businessUnitId!!
                            Log.d("TAG", "FuelIssueScreen:selectedFuelTypeId = $selectedFuelTypeId  selectedBusinessUnitId = $selectedBusinessUnitId ")

                            if (selectedFuelTypeId != null && selectedBusinessUnitId != null) {
                                sharedViewModel.fetchWarehouse(selectedBusinessUnitId, selectedFuelTypeId)
                            }
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
                onValueChange = {

                },
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
                warehousesName.forEach { warehouse ->
                    DropdownMenuItem(
                        text = { Text(warehouse) },
                        onClick = {
                            try {

                                selectedWarehouse = warehouse
                                warehouseExpanded = false
                                val selectedFuelTypeId = sharedViewModel.fuelTypes.value?.find { it.itemType == selectedFuelType }?.itemId!!
                                val selectedBusinessUnitId = sharedViewModel.businessType.value?.find { it.businessUnitDesc == selectedBusinessUnit }?.businessUnitId!!
                                val selectedWarehouseId = sharedViewModel.warehouse.value?.find { it.warehouseDesc == selectedWarehouse }?.warehouseId!!
                                Log.d("TAG", "FuelIssueScreen:selectedFuelTypeId = $selectedFuelTypeId  selectedBusinessUnitId = $selectedBusinessUnitId ")
                                if (selectedFuelTypeId != null && selectedBusinessUnitId != null && selectedWarehouseId != null) {
                                    sharedViewModel.selectedFuelTypeId.value = selectedFuelTypeId
                                    sharedViewModel.selectedBusinessUnitId.value = selectedBusinessUnitId
                                    sharedViewModel.selectedWarehouseId.value = selectedWarehouseId
                                    Log.d("TAG", "FuelIssueScreen: selectedWarehouseId = $selectedWarehouseId")
                                    sharedViewModel.selectedFuelTypeName.value = selectedFuelType
                                    sharedViewModel.selectedBusinessUnitName.value = selectedBusinessUnit
                                    sharedViewModel.selectedWarehouseName.value = selectedWarehouse

                                    Log.d(TAG, "FuelIssueScreen: selectedFuelTypeId ${sharedViewModel.selectedFuelTypeId.value}")

                                    sharedViewModel.fetchStockQuantity(selectedBusinessUnitId, selectedFuelTypeId,selectedWarehouseId)
                                }
                            }catch (e: Exception){
                                Toast.makeText(context, "Unfortunate Error Occurred!\n ${e.message}", Toast.LENGTH_SHORT).show()
                            }

                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Stock
        ReadOnlyNoFocusField("Stock",stockDisplay)
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                navController.navigate(Route.VEHICLE_ALLOCATION){
                    popUpTo(Route.MODULE_LIST) { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Next")
        }
        LoaderDialog(isShowing = isLoading)
    }
}

@Composable
fun ReadOnlyNoFocusField(label: String, value: String) {
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

/*@Composable
fun ReadOnlyNoFocusField(value: String) {
    val interactionSource = remember { MutableInteractionSource() }

    OutlinedTextField(
        value = value,
        onValueChange = { },
        modifier = Modifier
            .fillMaxWidth()
            .focusProperties { canFocus = false }, // prevent gaining focus
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        readOnly = true,
        interactionSource = interactionSource, // suppress ripple/focus visuals
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.Gray,
            focusedLabelColor = Color.Black,
            unfocusedLabelColor = Color.Black,
            textColor = Color.Black,
            cursorColor = Color.Transparent,
            disabledTextColor = Color.Black // in case something treats it as disabled internally
        )
    )
}*/


/*
@Preview(showBackground = true)
@Composable
fun FuelIssueScreenPreview() {
    val dummyNavController = rememberNavController()
    MVDugarGroupTheme {
        FuelIssueScreen(navController = dummyNavController)
    }
}*/
