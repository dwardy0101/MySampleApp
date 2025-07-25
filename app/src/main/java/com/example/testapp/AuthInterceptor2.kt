package com.example.testapp

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor2 : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

//        val credentials = "fdc_user:admin123"
//        val base64 = Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

        val original = chain.request()
        val requestBuilder = original.newBuilder()
//            .header("Authorization", "Basic $base64")
            .header("Accept", "application/json")
            .method(original.method, original.body)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
