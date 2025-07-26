package com.example.testapp.data

import android.util.Log
import com.example.testapp.data.api.AuthApi
import com.example.testapp.data.persistent.TokenProvider
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.HttpException

class AuthInterceptor(
    private val tokenProvider: TokenProvider,
    private val api: AuthApi
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        Log.d("MYTEST", "REQ-URL: ${originalRequest.url}")

        if (originalRequest.url.encodedPath.endsWith("/auth/login") ||
            originalRequest.url.encodedPath.endsWith("/auth/refresh-token")) {
            return chain.proceed(originalRequest)
        }

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

        response.close()

        // 401 case
        val newAccessToken = runBlocking {
            val refreshToken = tokenProvider.getRefreshToken() ?: return@runBlocking null

            try {
                // request new token
                val refreshResponse = api.refreshToken(mapOf("refreshToken" to refreshToken))

                if (
                    refreshResponse["access_token"].isJsonNull ||
                    refreshResponse["refresh_token"].isJsonNull
                ) {
                    tokenProvider.clearTokens()
                    return@runBlocking null
                }

                val newToken = refreshResponse["access_token"].toString()
                tokenProvider.saveAccessToken(newToken)

                val newRefreshToken = refreshResponse["refresh_token"].toString()
                tokenProvider.saveRefreshToken(newRefreshToken)

                return@runBlocking newToken

            } catch (e: HttpException) {
                e.printStackTrace()
                tokenProvider.clearTokens()
                return@runBlocking null

            } catch (e: Exception) {
                e.printStackTrace()
                tokenProvider.clearTokens()
                return@runBlocking null
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
