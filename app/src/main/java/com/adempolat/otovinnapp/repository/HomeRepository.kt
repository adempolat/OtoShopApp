package com.adempolat.otovinnapp.repository

import com.adempolat.otovinnapp.data.response.DiscoverData
import com.adempolat.otovinnapp.data.response.DiscoverResponse
import com.adempolat.otovinnapp.service.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class HomeRepository(private val apiService: ApiService) {
    fun fetchDiscoverData(token: String): Flow<DiscoverResponse> = flow {
        val response = apiService.getDiscoverData("Bearer $token")
        emit(response)
    }.catch { e ->
        emit(DiscoverResponse(code = 101, data = DiscoverData(emptyList(), emptyList(), emptyList(), emptyList()), message = e.message ?: "Unknown error"))
    }
}
