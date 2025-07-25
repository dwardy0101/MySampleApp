package com.example.testapp.data

import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("users")
    suspend fun createUser(@Body userData: UserData): JsonObject
}