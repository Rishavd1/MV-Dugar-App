package com.example.mvdugargroup.Api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
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


    @GET("FuelIssueRequest/api/FuelIssueRequest/GetExistingEntrySearch")
    suspend fun fetchExistingEntry(@Query("From_Date") fromDate: String,
                                   @Query("To_Date") toDate: String): Response<FuelExistingEntryResponse>

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

}
