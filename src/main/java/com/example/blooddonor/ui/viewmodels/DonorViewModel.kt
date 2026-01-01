package com.example.blooddonor.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blooddonor.data.models.User
import com.example.blooddonor.data.repository.DonorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DonorViewModel @Inject constructor(
    private val repository: DonorRepository
) : ViewModel() {

    private val _donors = MutableStateFlow<List<User>>(emptyList())
    val donors: StateFlow<List<User>> = _donors.asStateFlow()

    private val _uiState = MutableStateFlow<DonorUiState>(DonorUiState.Idle)
    val uiState: StateFlow<DonorUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow(SearchQuery())
    val searchQuery = _searchQuery.asStateFlow()

    init {
        searchDonors() // Initial data load
    }

    fun updateSearchQuery(bloodType: String = "", city: String = "") {
        _searchQuery.value = SearchQuery(bloodType, city)
        searchDonors()
    }

    fun searchDonors() {
        viewModelScope.launch {
            _uiState.value = DonorUiState.Loading
            repository.searchDonors(
                _searchQuery.value.bloodType,
                _searchQuery.value.city
            ).collectLatest { donorsList ->
                // Apply additional filtering in the ViewModel
                val filteredList = donorsList.filter {
                    (_searchQuery.value.bloodType.isEmpty() || it.bloodType == _searchQuery.value.bloodType) &&
                            (_searchQuery.value.city.isEmpty() || it.city.contains(_searchQuery.value.city, ignoreCase = true))
                }
                _donors.value = filteredList
                _uiState.value = DonorUiState.Success
            }
        }
    }

    fun addDonor(user: User) {
        viewModelScope.launch {
            _uiState.value = DonorUiState.Loading
            val result = repository.addDonor(user)
            _uiState.value = when {
                result.isSuccess -> DonorUiState.Success
                else -> DonorUiState.Error(result.exceptionOrNull()?.message ?: "Failed to add donor")
            }
        }
    }
}

data class SearchQuery(
    val bloodType: String = "",
    val city: String = ""
)

sealed class DonorUiState {
    object Idle : DonorUiState()
    object Loading : DonorUiState()
    object Success : DonorUiState()
    data class Error(val message: String) : DonorUiState()
}