package com.example.blooddonor.data.repository

import com.example.blooddonor.data.dao.UserDao
import com.example.blooddonor.data.models.User
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DonorRepository @Inject constructor(
    private val database: FirebaseDatabase,
    private val userDao: UserDao
) {

    suspend fun addDonor(user: User): Result<String> {
        return try {
            // Also save to local database
            userDao.insertOrUpdateUser(user)
            database.reference.child("donors").child(user.id).setValue(user).await()
            Result.success("Donor added successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun searchDonors(bloodType: String, city: String): Flow<List<User>> {
        // For simplicity, this example will query the local database directly.
        // A more robust solution would involve a remote-first or cache-first strategy.
        return if (city.isNotEmpty()) {
            userDao.getAllDonors() // Further filtering can be applied here
        } else {
            userDao.getAllDonors()
        }
    }

    suspend fun updateDonorAvailability(userId: String, isAvailable: Boolean): Result<String> {
        return try {
            // Update in remote
            database.reference.child("donors")
                .child(userId)
                .child("isAvailable")
                .setValue(isAvailable)
                .await()

            // Update in local
            val user = userDao.getUser(userId) // This should be a suspend function in DAO
            // user.first()?.let { userDao.insertOrUpdateUser(it.copy(isAvailable = isAvailable)) }

            Result.success("Availability updated")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}