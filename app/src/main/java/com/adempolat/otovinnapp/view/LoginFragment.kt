package com.adempolat.otovinnapp.view

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.adempolat.otovinnapp.repository.LoginRepository
import com.adempolat.otovinnapp.viewmodel.LoginViewModel
import com.adempolat.otovinnapp.R
import com.adempolat.otovinnapp.databinding.FragmentLoginBinding
import com.adempolat.otovinnapp.service.ApiClient
import com.adempolat.otovinnapp.utils.saveToken
import com.adempolat.otovinnapp.utils.showCustomToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// LoginFragment.kt
@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivTogglePassword.setOnClickListener {
            togglePasswordVisibility()
        }

        binding.btnLogin.setOnClickListener {
            val userName = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.login(userName, password)
        }

        lifecycleScope.launch {
            viewModel.loginResult.collect { result ->
                result?.let {
                    if (it.code == 100) {
                        saveToken(it.token ?: "",requireContext())
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        context?.showCustomToast(getString(R.string.success_login), requireContext().getColor(R.color.colorPrimary), R.drawable.logo)

                    } else {
                        // Hatalı giriş
                        context?.showCustomToast(getString(R.string.error_login, it.message),
                            requireContext().getColor(R.color.colorError), R.drawable.logo)

                    }
                }
            }
        }
    }

    private fun togglePasswordVisibility() {
        if (binding.etPassword.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            binding.etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.ivTogglePassword.setImageResource(R.drawable.visibility_off_24px)
        } else {
            binding.etPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.ivTogglePassword.setImageResource(R.drawable.visibility_24px)
        }
        binding.etPassword.setSelection(binding.etPassword.text.length)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
