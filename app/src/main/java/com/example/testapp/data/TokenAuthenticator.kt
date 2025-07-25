package com.example.testapp.data

import android.util.Log
import com.example.testapp.data.api.AuthApi
import com.example.testapp.data.persistent.TokenProvider
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val tokenProvider: TokenProvider,
    private val apiService: AuthApi
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // Avoid infinite loop
        if (responseCount(response) >= 2) return null

        return runBlocking {
            val refreshToken = tokenProvider.getRefreshToken() ?: return@runBlocking null

            val refreshResponse = apiService.refreshToken(mapOf("refreshToken" to refreshToken))

            Log.d("MYTEST", "RESULT : $refreshResponse")

            refreshResponse["access_token"] ?: return@runBlocking null

            val newAccessToken = refreshResponse["access_token"].toString()

            tokenProvider.saveAccessToken(newAccessToken)

            response.request.newBuilder()
                .header("Authorization", "Bearer $newAccessToken")
                .build()
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var res = response.priorResponse
        while (res != null) {
            count++
            res = res.priorResponse
        }
        return count
    }

}
