package com.example.testapp.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.testapp.Network
import com.example.testapp.R
import com.example.testapp.data.UserApi
import com.example.testapp.data.UserData
import com.example.testapp.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(binding.main.id)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.create.setOnClickListener {
            val api = Network.createService(UserApi::class.java)

            val input = UserData(
                binding.textInputEditText.text.toString(),
                binding.textInputEditText2.text.toString(),
                binding.textInputEditText3.text.toString()
            )

            lifecycleScope.launch {
                val response = api.createUser(input)

                Log.d("MYTEST", "$response")
            }
        }
    }
}