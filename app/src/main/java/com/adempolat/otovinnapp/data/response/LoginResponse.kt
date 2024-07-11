package com.adempolat.otovinnapp.data.response

data class LoginResponse(
    val code: Int,
    val token: String? = null,
    val message: String? = null
)