package com.adempolat.otovinnapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

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
