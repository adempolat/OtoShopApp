package com.adempolat.otovinnapp

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.adempolat.otovinnapp.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(LoginRepository(ApiClient.apiService))
    }

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
                        // Başarılı giriş
                        Toast.makeText(context, "Giriş Başarılı: ${it.token}", Toast.LENGTH_SHORT).show()
                    } else {
                        // Hatalı giriş
                        Toast.makeText(context, "Giriş Hatası: ${it.message}", Toast.LENGTH_SHORT).show()
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

