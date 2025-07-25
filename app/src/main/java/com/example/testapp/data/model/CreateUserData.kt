package com.example.testapp.data.model

import com.google.gson.annotations.SerializedName

data class CreateUserData(
    @SerializedName("name")
    private val name: String,
    @SerializedName("email")
    private val email: String,
    @SerializedName("password")
    private val password: String,
    @SerializedName("avatar")
    private val avatarUrl: String = "https://placehold.co/400"
)
