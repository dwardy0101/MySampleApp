package com.example.testapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.testapp.data.persistent.TokenProviderImpl
import com.example.testapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        lifecycleScope.launch {
            val tokenProvider = TokenProviderImpl(this@MainActivity)

            if (tokenProvider.getAccessToken().isNullOrEmpty()) {
                startActivity(
                    Intent(this@MainActivity, LoginActivity::class.java).apply {
                        setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    })
                finish()
            } else {
                setContentView(binding.main)

                ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
                    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                    insets
                }

                initView()
            }
        }

        val view = View(this).apply {
            id = View.generateViewId()
        }

        setContentView(view)

        binding.logout.setOnClickListener {
            lifecycleScope.launch {
                TokenProviderImpl(this@MainActivity).apply {
                    clearTokens()
                }

                startActivity(
                    Intent(this@MainActivity, LoginActivity::class.java).apply {
                        setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                )
            }
        }

    }

    private fun initView() {
        val adapter = PagerAdapter(supportFragmentManager)
        binding.pager.adapter = adapter
    }
}