package com.example.testapp

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Network {

    private lateinit var retrofit: Retrofit

    private var builder: Retrofit.Builder = Retrofit.Builder()
        .baseUrl("https://api.escuelajs.co/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())


    private val httpClient: OkHttpClient.Builder
            = OkHttpClient.Builder()

    private val logging:HttpLoggingInterceptor
            = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BASIC)

    fun <T> createService(serviceClass: Class<T>): T {
        if (!httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging)
            httpClient.addInterceptor(AuthInterceptor2())
            builder.client(httpClient.build())
            retrofit = builder.build()
        }
        return retrofit.create(serviceClass)
    }
}