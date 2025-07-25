package com.example.testapp.data.api

import com.example.testapp.data.model.LoginUserData
import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body loginUserData: LoginUserData): JsonObject

    @POST("auth/refresh-token")
    suspend fun refreshToken(@Body body: Map<String, String>): JsonObject
}