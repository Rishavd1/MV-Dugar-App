package com.example.mvdugargroup.Api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("User/Login")
    suspend fun login(
        @Query("UserName") userName: String?,
        @Query("Password") password: String?
    ): Response<LoginResponse>
}
