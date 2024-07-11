package com.adempolat.otovinnapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adempolat.otovinnapp.data.response.LoginResponse
import com.adempolat.otovinnapp.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


// LoginViewModel.kt
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
) : ViewModel() {

    private val _loginResult = MutableStateFlow<LoginResponse?>(null)
    val loginResult: StateFlow<LoginResponse?> = _loginResult.asStateFlow()

    fun login(userName: String, password: String) {
        viewModelScope.launch {
            repository.login(userName, password).collect { response ->
                _loginResult.value = response
            }
        }
    }
}

