package com.example.blooddonor.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blooddonor.data.dao.UserDao
import com.example.blooddonor.data.models.User
import com.example.blooddonor.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userDao: UserDao
) : ViewModel() {

    private val userId = authRepository.getCurrentUser()?.uid ?: ""

    val userProfile: StateFlow<User?> = userDao.getUser(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            // You might want to clear local data upon logout
            userDao.deleteAllUsers()
        }
    }
}