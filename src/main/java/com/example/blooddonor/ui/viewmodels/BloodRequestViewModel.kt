package com.example.blooddonor.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blooddonor.data.models.BloodRequest
import com.example.blooddonor.data.repository.BloodRequestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BloodRequestViewModel @Inject constructor(
    private val repository: BloodRequestRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<BloodRequestUiState>(BloodRequestUiState.Idle)
    val uiState: StateFlow<BloodRequestUiState> = _uiState

    fun submitRequest(request: BloodRequest) {
        viewModelScope.launch {
            _uiState.value = BloodRequestUiState.Loading
            val result = repository.submitRequest(request)
            _uiState.value = when {
                result.isSuccess -> BloodRequestUiState.Success("Request submitted successfully")
                else -> BloodRequestUiState.Error(result.exceptionOrNull()?.message ?: "Failed to submit request")
            }
        }
    }
}

sealed class BloodRequestUiState {
    object Idle : BloodRequestUiState()
    object Loading : BloodRequestUiState()
    data class Success(val message: String) : BloodRequestUiState()
    data class Error(val message: String) : BloodRequestUiState()
}
