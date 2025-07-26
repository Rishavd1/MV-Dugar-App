package com.example.mvdugargroup.Api

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val UserName: String,
    val Password: String
)

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