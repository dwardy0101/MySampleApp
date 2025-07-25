package com.example.testapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.testapp.MainActivity
import com.example.testapp.Network
import com.example.testapp.data.RetrofitProvider
import com.example.testapp.data.api.AuthApi
import com.example.testapp.data.model.LoginUserData
import com.example.testapp.data.persistent.TokenProviderImpl
import com.example.testapp.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.main)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.login.setOnClickListener {
            val api = RetrofitProvider
                .createRetrofit(TokenProviderImpl(this))
                .create(AuthApi::class.java)

            val input = LoginUserData(
                binding.textInputEditText.text.toString(),
                binding.textInputEditText2.text.toString()
            )

            lifecycleScope.launch {
                val result = api.login(input)

                Log.d("MYTEST", "$result")

                TokenProviderImpl(this@LoginActivity).apply {
                    saveAccessToken(result["access_token"].toString())
                    saveRefreshToken(result["refresh_token"].toString())
                }

                startActivity(
                    Intent(this@LoginActivity, MainActivity::class.java).apply {
                        setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                )
                finish()
            }

        }

        binding.register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.reset.setOnClickListener {
           lifecycleScope.launch {
               TokenProviderImpl(this@LoginActivity).clearTokens()
           }
        }
    }
}