package com.example.mvdugargroup.Api

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
