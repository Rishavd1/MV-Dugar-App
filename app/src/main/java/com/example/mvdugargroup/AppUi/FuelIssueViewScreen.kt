package com.example.mvdugargroup.AppUi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.style.TextOverflow
import com.example.mvdugargroup.Route
import com.example.mvdugargroup.utils.DeleteConfirmationDialog
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelIssueViewScreen(
    navController: NavController? = null,
    sharedViewModel: SharedViewModel = viewModel()
) {
    val bottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    // Filter states
    var selectedFuelType by remember { mutableStateOf("Select Fuel Type") }
    var selectedVehicle by remember { mutableStateOf("") }
    var selectedBusinessUnit by remember { mutableStateOf("") }
    var selectedWarehouse by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf(LocalDate.now().minusMonths(1)) }
    var endDate by remember { mutableStateOf(LocalDate.now()) }

    var itemToDelete by remember { mutableStateOf<FuelIssueItem?>(null) }


    // Dropdown items
    val vehicleList = listOf(
        "LMVMBC001 - BOLERO CAMPER BSIII - BA",
        "LMVMBC002 - TATA ACE - BU",
        "LMVMBC003 - MAHINDRA MAXX - AM"
    )
    val businessUnits = listOf("Unit A", "Unit B", "Unit C")
    val warehouses = listOf("WH-101", "WH-102", "WH-103")
    val fuelTypes = listOf("DIESEL HIGH SPEED", "PETROL", "CNG")

    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val searchResults = remember { mutableStateListOf<FuelIssueItem>() }


    LaunchedEffect(Unit) {
        searchResults.clear()
        searchResults.addAll(mockFuelData()) // Load dummy data when screen opens
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
            if (searchResults.isNotEmpty()) {
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
                    itemsIndexed(searchResults) { index, item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
                            elevation = CardDefaults.cardElevation(4.dp)
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
                                        "Issue No: ${item.issueNo}",
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
                                    if (itemToDelete != null) {
                                        DeleteConfirmationDialog(
                                            itemName = "Issue No: ${itemToDelete!!.issueNo}",
                                            onConfirm = {
                                                searchResults.remove(itemToDelete)
                                                itemToDelete = null  // Close dialog
                                            },
                                            onDismiss = {
                                                itemToDelete =
                                                    null  // Close dialog without deleting
                                            }
                                        )
                                    }
                                }

                                Spacer(Modifier.height(8.dp))

                                InfoRow(label = "Fuel Type", value = item.fuelType)
                                InfoRow(
                                    label = "Issue Date",
                                    value = item.issueDate.format(dateFormatter)
                                )
                                InfoRow(label = "Business Unit", value = item.businessUnit)
                                InfoRow(label = "Warehouse", value = item.warehouse)
                                InfoRow(label = "Vehicle", value = item.vehicleDetail)
                            }
                        }
                    }
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
                        expanded = false,
                        items = fuelTypes,
                        onExpandedChange = {},
                        onItemSelected = { selectedFuelType = it }
                    )

                    DropdownField(
                        label = "Vehicle",
                        value = selectedVehicle,
                        expanded = false,
                        items = vehicleList,
                        onExpandedChange = {},
                        onItemSelected = { selectedVehicle = it }
                    )

                    DropdownField(
                        label = "Business Unit",
                        value = selectedBusinessUnit,
                        expanded = false,
                        items = businessUnits,
                        onExpandedChange = {},
                        onItemSelected = { selectedBusinessUnit = it }
                    )

                    DropdownField(
                        label = "Warehouse",
                        value = selectedWarehouse,
                        expanded = false,
                        items = warehouses,
                        onExpandedChange = {},
                        onItemSelected = { selectedWarehouse = it }
                    )

                    Spacer(Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(Modifier.weight(1f)) {
                            Text("From Date", fontWeight = FontWeight.SemiBold)
                            OutlinedTextField(
                                value = startDate.format(dateFormatter),
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { /* date picker */ },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text("To Date", fontWeight = FontWeight.SemiBold)
                            OutlinedTextField(
                                value = endDate.format(dateFormatter),
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { /* date picker */ },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = {
                                selectedFuelType = "Select Fuel Type"
                                selectedVehicle = ""
                                selectedBusinessUnit = ""
                                selectedWarehouse = ""
                                startDate = LocalDate.now().minusMonths(1)
                                endDate = LocalDate.now()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Reset")
                        }

                        Spacer(Modifier.width(16.dp))

                        Button(
                            onClick = {
                                showBottomSheet = false
                                searchResults.clear()
                                searchResults.addAll(mockFuelData()) // Apply actual filter logic
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
    ),
    FuelIssueItem(
        fuelType = "DIESEL HIGH SPEED",
        issueNo = "LKFISS/00012/24-25",
        issueDate = LocalDate.of(2025, 6, 13),
        businessUnit = "LAPCHE KHOLA - SUMO",
        warehouse = "Main Store Lapche Khola",
        vehicleDetail = "TATA TIPPER 1613"
    ),
    FuelIssueItem(
        fuelType = "DIESEL HIGH SPEED",
        issueNo = "LKFISS/00013/24-25",
        issueDate = LocalDate.of(2025, 6, 13),
        businessUnit = "LAPCHE KHOLA - SUMO",
        warehouse = "Main Store Lapche Khola",
        vehicleDetail = "TATA TIPPER 1613"
    ),
    FuelIssueItem(
        fuelType = "DIESEL HIGH SPEED",
        issueNo = "LKFISS/00014/24-25",
        issueDate = LocalDate.of(2025, 6, 13),
        businessUnit = "LAPCHE KHOLA - SUMO",
        warehouse = "Main Store Lapche Khola",
        vehicleDetail = "TATA TIPPER 1613"
    ),
    FuelIssueItem(
        fuelType = "DIESEL HIGH SPEED",
        issueNo = "LKFISS/00015/24-25",
        issueDate = LocalDate.of(2025, 6, 13),
        businessUnit = "LAPCHE KHOLA - SUMO",
        warehouse = "Main Store Lapche Khola",
        vehicleDetail = "TATA TIPPER 1613"
    ),
    FuelIssueItem(
        fuelType = "DIESEL HIGH SPEED",
        issueNo = "LKFISS/00016/24-25",
        issueDate = LocalDate.of(2025, 6, 13),
        businessUnit = "LAPCHE KHOLA - SUMO",
        warehouse = "Main Store Lapche Khola",
        vehicleDetail = "TATA TIPPER 1613"
    ),
    FuelIssueItem(
        fuelType = "DIESEL HIGH SPEED",
        issueNo = "LKFISS/00017/24-25",
        issueDate = LocalDate.of(2025, 6, 13),
        businessUnit = "LAPCHE KHOLA - SUMO",
        warehouse = "Main Store Lapche Khola",
        vehicleDetail = "TATA TIPPER 1613"
    ),
    FuelIssueItem(
        fuelType = "DIESEL HIGH SPEED",
        issueNo = "LKFISS/00018/24-25",
        issueDate = LocalDate.of(2025, 6, 13),
        businessUnit = "LAPCHE KHOLA - SUMO",
        warehouse = "Main Store Lapche Khola",
        vehicleDetail = "TATA TIPPER 1613"
    ),
    FuelIssueItem(
        fuelType = "DIESEL HIGH SPEED",
        issueNo = "LKFISS/00019/24-25",
        issueDate = LocalDate.of(2025, 6, 13),
        businessUnit = "LAPCHE KHOLA - SUMO",
        warehouse = "Main Store Lapche Khola",
        vehicleDetail = "TATA TIPPER 1613"
    ),
    FuelIssueItem(
        fuelType = "DIESEL HIGH SPEED",
        issueNo = "LKFISS/00020/24-25",
        issueDate = LocalDate.of(2025, 6, 13),
        businessUnit = "LAPCHE KHOLA - SUMO",
        warehouse = "Main Store Lapche Khola",
        vehicleDetail = "TATA TIPPER 1613"
    )
    // Add more...
)
