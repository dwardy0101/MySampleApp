package com.example.testapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.testapp.R
import com.example.testapp.data.RetrofitProvider
import com.example.testapp.data.api.AuthApi
import com.example.testapp.data.persistent.TokenProviderImpl
import com.example.testapp.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        binding.getProfile.setOnClickListener {
            getProfile()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getProfile()
    }

    private fun getProfile() {
        lifecycleScope.launch {
            val api = RetrofitProvider
                .createRetrofit(TokenProviderImpl(requireContext()))
                .create(AuthApi::class.java)

            try {
                val result = api.getProfile()
                Log.d("MYTEST", "$result")

                UserManager.createUser(result)

                val user = UserManager.user!!
                binding.userId.text = user.id.toString()
                binding.userEmail.text = user.email
                binding.userName.text = user.name
                binding.userRole.text = user.role
                binding.userAvatar.text = user.avatar

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}