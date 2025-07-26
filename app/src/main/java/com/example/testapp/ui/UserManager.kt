package com.example.testapp.ui

import com.example.testapp.ui.model.User
import com.google.gson.Gson
import com.google.gson.JsonObject

object UserManager {

    var user: User? = null

    fun createUser(json: JsonObject) {
        user = Gson().fromJson(json, User::class.java)
    }
}