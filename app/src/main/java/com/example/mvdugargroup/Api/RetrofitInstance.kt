package com.example.mvdugargroup.Api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://your.actual.base.url/"
    val api: ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // 🔁 Replace with actual base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}