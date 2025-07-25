package com.example.testapp.data

import com.example.testapp.data.api.AuthApi
import com.example.testapp.data.persistent.TokenProvider
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenProvider: TokenProvider,
    private val api: AuthApi
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Get current token synchronously
        val accessToken = runBlocking {
            tokenProvider.getAccessToken()
        }

        // Attach Authorization header
        val requestWithToken = originalRequest.newBuilder().apply {
            accessToken?.let {
                header("Authorization", "Bearer $it")
            }
        }.build()

        val response = chain.proceed(requestWithToken)

        // If not unauthorized, return as is
        if (response.code != 401) {
            return response
        }

        response.close() // important: close old response

        // Try to refresh token
        val newAccessToken = runBlocking {
            val refreshToken = tokenProvider.getRefreshToken() ?: return@runBlocking null

            try {
                val refreshResponse = api.refreshToken(mapOf("refreshToken" to refreshToken))

                refreshResponse["access_token"] ?: return@runBlocking null

                val newToken = refreshResponse["access_token"].toString()
                tokenProvider.saveAccessToken(newToken)

                return@runBlocking newToken

            } catch (e : Exception) {
                e.printStackTrace()
            }
        }

        // If refresh fails, don't retry
        if (newAccessToken == null) {
            return response
        }

        // Retry request with new token
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $newAccessToken")
            .build()

        return chain.proceed(newRequest)
    }
}
