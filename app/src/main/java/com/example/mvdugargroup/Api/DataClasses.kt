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