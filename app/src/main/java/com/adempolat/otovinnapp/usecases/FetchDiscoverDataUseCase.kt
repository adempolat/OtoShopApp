package com.adempolat.otovinnapp.usecases

import com.adempolat.otovinnapp.data.response.DiscoverResponse
import com.adempolat.otovinnapp.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchDiscoverDataUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(token: String): Flow<DiscoverResponse> {
        return homeRepository.fetchDiscoverData(token)
    }
}
