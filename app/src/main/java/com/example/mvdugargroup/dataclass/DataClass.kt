package com.example.mvdugargroup.dataclass

import java.time.LocalDate

data class FuelIssueItem(
    val fuelType: String,
    val issueNo: String,
    val issueDate: LocalDate,
    val businessUnit: String,
    val warehouse: String,
    val vehicleDetail: String
)
