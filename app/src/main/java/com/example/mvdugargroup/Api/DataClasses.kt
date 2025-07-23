package com.example.mvdugargroup.Api

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val token: String, val userId: String)