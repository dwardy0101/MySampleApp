package com.example.testapp.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.testapp.data.RetrofitProvider
import com.example.testapp.data.api.UserApi
import com.example.testapp.data.model.CreateUserData
import com.example.testapp.data.persistent.TokenProviderImpl
import com.example.testapp.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.main)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.create.setOnClickListener {
            signUp()
        }
    }

    private fun signUp() {
        if (binding.textInputEditText.text.isNullOrEmpty()) {
            binding.textInputEditText.error = "Invalid input!"
            return
        }

        if (
            binding.textInputEditText2.text.isNullOrEmpty() ||
            !isValidEmail(binding.textInputEditText2.text.toString())
        ) {
            binding.textInputEditText2.error = "Invalid input!"
            return
        }

        if (binding.textInputEditText3.text.isNullOrEmpty()) {
            binding.textInputEditText3.error = "Invalid input!"
            return
        }

        val api = RetrofitProvider
            .createRetrofit(TokenProviderImpl(this))
            .create(UserApi::class.java)

        val input = CreateUserData(
            binding.textInputEditText.text.toString(),
            binding.textInputEditText2.text.toString(),
            binding.textInputEditText3.text.toString()
        )

        lifecycleScope.launch {
            try {
                val response = api.createUser(input)
                Log.d("MYTEST", "$response")

                if (response.isJsonNull) {
                    Toast.makeText(this@RegisterActivity, "Registration Failed!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@RegisterActivity, "Registration Successful!", Toast.LENGTH_LONG).show()
                    finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
        return emailRegex.matches(email)
    }
}