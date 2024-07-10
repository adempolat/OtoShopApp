package com.adempolat.otovinnapp

data class LoginResponse(
    val code: Int,
    val token: String? = null,
    val message: String? = null
)