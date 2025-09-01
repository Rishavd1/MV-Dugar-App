package com.example.mvdugargroup.Api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiInterface {
    @GET("User/Login")
    suspend fun login(
        @Query("UserName") userName: String?,
        @Query("Password") password: String?
    ): Response<LoginResponse>

    @GET("FuelIssueRequest/api/FuelIssueRequest/GetItem")
    suspend fun fetchFuelTypes(): Response<FuelTypeResponse>

    @GET("FuelIssueRequest/api/FuelIssueRequest/GetBusinessUnit")
    suspend fun fetchBusinessUnit(): Response<BusinessUnitResponse>

    //fetch warehouses based on fuel type & business unit
    @GET("FuelIssueRequest/api/FuelIssueRequest/GetWarehouseSearch")
    suspend fun fetchWarehouse(
        @Query("BUId") businessUnitId: Int,
        @Query("ItemId") fuelTypeId: Int
    ): Response<WarehouseResponse>

    @GET("FuelIssueRequest/api/FuelIssueRequest/GetStockFetch")
    suspend fun fetchStockQuantity(
        @Query("BUId") businessUnitId: Int,
        @Query("ItemId") fuelTypeId: Int,
        @Query("WHId") warehouseId: Int
    ): Response<StockQuantityResponse>

    @GET("FuelIssueRequest/api/FuelIssueRequest/GetMeterStatus")
    suspend fun fetchMeterStatus(): Response<MeterStatusResponse>


//    @GET("FuelIssueRequest/api/FuelIssueRequest/GetExistingEntrySearch")
//    suspend fun fetchExistingEntry(@Query("From_Date") fromDate: String,
//                                   @Query("To_Date") toDate: String): Response<FuelExistingEntryResponse>

    @GET("FuelIssueRequest/api/FuelIssueRequest/GetExistingEntrySearch")
    suspend fun fetchExistingEntry(
        @Query("From_Date") fromDate: String,
        @Query("To_Date") toDate: String,
        @Query("ItemType") itemType: String? = null,
        @Query("BUDesc") buDesc: String? = null,
        @Query("WHDesc") whDesc: String? = null,
        @Query("VehicleName") vehicleName: String? = null
    ): Response<FuelExistingEntryResponse>
    @GET("FuelIssueRequest/api/FuelIssueRequest/GetVehicleSearch")
    suspend fun fetchVehicleList(@Query("ItemId") fuelTypeId: Int) : Response<VehicleListResponse>


    @Multipart
    @POST("FuelIssueRequest/api/FuelIssueRequest/Submit")
    suspend fun submitFuelIssue(
        @Part("data") data: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<Unit>


    @GET("FuelIssueRequest/api/FuelIssueRequest/GetPrevReadingFetch")
    suspend fun fetchPrevReadingFetch(
        @Query("VehicleName") vehicleName: String,
        @Query("IssueDate") issueDate: String
    ): Response<PreviousReadingResponse>

    @Multipart
    @POST("FuelIssueRequest/api/FuelIssueRequest/Submit")
    suspend fun submitFuelIssue(
        @Part("Standard_ConsT") standardConsT: RequestBody,
        @Part("WHDesc") whDesc: RequestBody,
        @Part("CostCenter") costCenter: RequestBody,
        @Part("BUDesc") buDesc: RequestBody,
        @Part("Current_Reading") currentReading: RequestBody,
        @Part("IssueDate") issueDate: RequestBody,
        @Part("EntryBy") entryBy: RequestBody,
        @Part("AssetId") assetId: RequestBody,
        @Part("Quantity") quantity: RequestBody,
        @Part("WHId") whId: RequestBody,
        @Part("ItemType") itemType: RequestBody,
        @Part("Read_Unit") readUnit: RequestBody,
        @Part("BUId") buId: RequestBody,
        @Part("ItemId") itemId: RequestBody,
        @Part("Standard_Cons") standardCons: RequestBody,
        @Part("PrevIssueDate") prevIssueDate: RequestBody,
        @Part("Stock") stock: RequestBody,
        @Part("VehicleName") vehicleName: RequestBody,
        @Part("PrevReading") prevReading: RequestBody,
        @Part("MeterStatus") meterStatus: RequestBody,
        @Part file: MultipartBody.Part
    ): Response<ApiResponse>

    @Headers("Content-Type: application/json")
    @POST("FuelIssueRequest/api/FuelIssueRequest/Delete")
    suspend fun deleteFuelIssue(
        @Body request: DeleteFuelIssueRequest
    ): Response<ApiResponse>

}
