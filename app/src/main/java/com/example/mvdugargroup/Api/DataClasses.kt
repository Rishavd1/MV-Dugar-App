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
    val entryBy: String,
    val assetId: String,
    val costCenter: String,
    val issueQuanity: Double,
    val standardConsumptionType: String
)

@Serializable
data class FuelExistingEntryResponse(
    val statusCode: Int,
    val isSuccess: Int,
    val errorMessages : List<String>,
    val result: List<FuelExistingEntry>
)

@Serializable
data class FuelExistingEntry(
    val tranId: Long,
    val itemType: String,
    val itemId: Int,
    val requestNo: String,
    val issueDate: String,
    val buId: Int,
    val buDesc: String,
    val whId: Int,
    val whDesc: String,
    val assetId: Long,
    val costCenter: Int,
    val vehicleCode: String,
    val vehicleName: String,
    val quanity: Double,
    val standardQty: Double,
    val stock: Double,
    val read_Unit: String,
    val standard_Cons: Double,
    val standard_ConsT: String,
    val prevReading: Double,
    val prevIssueDate: String,
    val meterStatus: String,
    val current_Reading: Double,
    val remarks: String,
    val entryBy: String,
    val entryDate: String,
    val modifyBy: String,
    val modifyDate: String,
    val canDelete: Int
)


@Serializable
data class VehicleListResponse(
    val statusCode: Int,
    val isSuccess: Int,
    val result: List<VehicleList>
)

@Serializable
data class VehicleList(
    val slNo: Int,
    val code: String,
    val description: String
)

@Serializable
data class PreviousReadingResponse(
    val statusCode: Int,
    val isSuccess: Int,
    val result: PrevReadingResult
)

@Serializable
data class PrevReadingResult(
    val slNo: Int,
    val cC_Id: Int,
    val fA_Id: Int,
    val code: String,
    val unit: String,
    val st_Average: Double,
    val st_AverageT: String,
    val description: String,
    val preV_READING: Int,
    val preV_DATE: String,
    val diff_Perc: Int,
    val diff_Reading: Int
)

@Serializable
data class ApiResponse(
    val statusCode: Int,
    val isSuccess: Int,
    val errorMessages: List<String>,
    val result: ResultData
)

@Serializable
data class ResultData(
    val id: Int,
    val userName: String,
    val name: String,
    val status: Int,
    val message: String
)


