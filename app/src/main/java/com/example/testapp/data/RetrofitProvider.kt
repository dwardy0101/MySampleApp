package com.example.testapp.data

import com.example.testapp.data.api.AuthApi
import com.example.testapp.data.persistent.TokenProvider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitProvider {
    fun createRetrofit(tokenProvider: TokenProvider): Retrofit {
        val logging = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BASIC)

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenProvider, createApiForTokenRefresh()))
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.escuelajs.co/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    // this Retrofit is only for refreshing token — no AuthInterceptor to avoid loop
    private fun createApiForTokenRefresh(): AuthApi {
        val client = OkHttpClient.Builder().build()

        return Retrofit.Builder()
            .baseUrl("https://api.escuelajs.co/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(AuthApi::class.java)
    }
}
