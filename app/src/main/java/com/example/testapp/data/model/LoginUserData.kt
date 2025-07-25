package com.example.testapp.data.model

import com.google.gson.annotations.SerializedName

data class LoginUserData(
    @SerializedName("email")
    private val email: String,
    @SerializedName("password")
    private val password: String
)
