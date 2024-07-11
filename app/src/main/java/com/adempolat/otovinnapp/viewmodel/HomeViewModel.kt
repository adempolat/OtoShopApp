package com.adempolat.otovinnapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adempolat.otovinnapp.data.response.DiscoverResponse
import com.adempolat.otovinnapp.repository.HomeRepository
import com.adempolat.otovinnapp.usecases.FetchDiscoverDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchDiscoverDataUseCase: FetchDiscoverDataUseCase
) : ViewModel() {

    private val _discoverData = MutableStateFlow<DiscoverResponse?>(null)
    val discoverData: StateFlow<DiscoverResponse?> = _discoverData

    fun fetchDiscoverData(token: String) {
        viewModelScope.launch {
            fetchDiscoverDataUseCase(token).collect {
                _discoverData.value = it
            }
        }
    }
}


