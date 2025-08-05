package com.example.mvdugargroup.viewmodel

import com.example.mvdugargroup.Api.ApiInterface
import com.example.mvdugargroup.Api.FuelIssueRequest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

class FuelIssueRepository(private val api: ApiInterface) {


    suspend fun sendFuelIssueRequest(
        request: FuelIssueRequest,
        imageFile: File?
    ): Response<Unit> {
        val json = Json.encodeToString(request)
        val jsonRequestBody = json.toRequestBody("application/json".toMediaType())

        val imagePart = imageFile?.let {
            val imageRequestBody = it.asRequestBody("image/*".toMediaType())
            MultipartBody.Part.createFormData("image", it.name, imageRequestBody)
        }
        return api.submitFuelIssue(
            data = jsonRequestBody,
            image = imagePart
        )
    }
}