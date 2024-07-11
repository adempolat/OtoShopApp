package com.adempolat.otovinnapp.service

import com.adempolat.otovinnapp.data.response.DiscoverResponse
import com.adempolat.otovinnapp.data.request.LoginRequest
import com.adempolat.otovinnapp.data.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("testcase/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("testcase/discover")
    suspend fun getDiscoverData(@Header("Authorization") token: String): DiscoverResponse
}
