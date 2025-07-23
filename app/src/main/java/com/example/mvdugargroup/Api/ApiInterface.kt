package com.example.mvdugargroup.Api

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response



interface ApiInterface {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}