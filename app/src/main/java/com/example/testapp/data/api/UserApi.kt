package com.example.testapp.data.api

import com.example.testapp.data.model.CreateUserData
import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("users")
    suspend fun createUser(@Body createUserData: CreateUserData): JsonObject
}