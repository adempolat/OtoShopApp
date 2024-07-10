package com.adempolat.otovinnapp

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("testcase/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
