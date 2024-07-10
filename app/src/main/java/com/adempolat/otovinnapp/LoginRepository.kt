package com.adempolat.otovinnapp

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class LoginRepository(private val apiService: ApiService) {
    fun login(userName: String, password: String): Flow<LoginResponse> = flow {
        val response = apiService.login(LoginRequest(userName, password))
        emit(response)
    }.catch { e ->
        emit(LoginResponse(code = 101, message = e.message ?: "Unknown error"))
    }
}
