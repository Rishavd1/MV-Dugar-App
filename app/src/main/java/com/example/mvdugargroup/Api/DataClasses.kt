package com.example.mvdugargroup.Api

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val statusCode: Int,
    val result: LoginDetailsResponse
)

@Serializable
data class LoginDetailsResponse(
    val id: Int,
    val userName: String,
    val name: String
)

@Serializable
data class FuelTypeResponse(
    val statusCode: Int,
    val result: List<FuelType>
)

@Serializable
data class FuelType(
    val itemId: Int,
    val itemType: String
)

@Serializable
data class BusinessUnitResponse(
    val statusCode: Int,
    val result: List<BusinessUnit>
)


@Serializable
data class BusinessUnit(
    @SerializedName("buId")
    val businessUnitId: Int,
    @SerializedName("buDesc")
    val businessUnitDesc: String
)


@Serializable
data class WarehouseResponse(
    val statusCode: Int,
    val result: List<Warehouse>
)

@Serializable
data class Warehouse(
    @SerializedName("wareHouseId")
    val warehouseId: Int,
    @SerializedName("wareHouseDesc")
    val warehouseDesc: String
)

@Serializable
data class StockQuantityResponse(
    val statusCode: Int,
    val result: StockQuantity
)

@Serializable
data class StockQuantity(
    @SerializedName("closingStock")
    val stockQuantity: Double
)

@Serializable
data class MeterStatusResponse(
    val statusCode: Int,
    val result: List<MeterStatus>
)

@Serializable
data class MeterStatus(
    @SerializedName("meterstatus")
    val meterStatus: String
)

@Serializable
data class FuelIssueRequest(
    val issueNo: String,
    val issueDate: String,
    val fuelTypeId: Int,
    val fuelTypeName: String,
    val businessUnitId: Int,
    val businessUnitName: String,
    val warehouseId: Int,
    val warehouseName: String,
    val stock: Double,
    val vehicleName: String,
    val standardConsumption: Double,
    val previousReading: Double,
    val previousIssueDate: String,
    val meterStatus: String,
    val currentReading: Double,
    val entryBy: String
)

@Serializable
data class FuelExistingEntryResponse(
    val statusCode: Int,
    val isSuccess: Int,
    val result: FuelExistingEntry
)

@Serializable
data class FuelExistingEntry(
    val tranId: Int,
    val itemType: String,
    val itemId: Int,
    val requestNo: Int,
    val issueDate: String,
    val buId: Int,
    val buDesc: String,
    val whId: Int,
    val whDesc: Int,
    val assetId: Int,
    val costCenter: Int,
    val vehicleCode: String,
    val vehicleName: String,
    val quanity: Int,
    val standardQty: Int,
    val stock: Int,
    val read_Unit: Int,
    val standard_Cons: Int,
    val standard_ConsT: Int,
    val prevReading: Int,
    val prevIssueDate: String,
    val meterStatus: String,
    val current_Reading: Int,
    val remarks: String,
    val entryBy: String,
    val entryDate: String,
    val modifyBy: String,
    val modifyDate: String,
    val canDelete: Int
)



