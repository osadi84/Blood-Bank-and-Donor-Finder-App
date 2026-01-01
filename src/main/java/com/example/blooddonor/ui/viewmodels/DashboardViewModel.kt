package com.example.blooddonor.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blooddonor.data.repository.AuthRepository
import com.example.blooddonor.data.repository.DonorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val donorRepository: DonorRepository
) : ViewModel() {

    val currentUser = authRepository.getCurrentUser()

    fun updateUserAvailability(isAvailable: Boolean) {
        viewModelScope.launch {
            currentUser?.uid?.let {
                donorRepository.updateDonorAvailability(it, isAvailable)
            }
        }
    }
}