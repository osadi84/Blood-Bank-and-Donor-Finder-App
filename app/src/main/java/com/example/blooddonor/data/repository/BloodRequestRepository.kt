package com.example.blooddonor.data.repository

import com.example.blooddonor.data.models.BloodRequest
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class BloodRequestRepository @Inject constructor(
    private val database: FirebaseDatabase
) {

    suspend fun submitRequest(request: BloodRequest): Result<Unit> {
        return try {
            val requestId = UUID.randomUUID().toString()
            val newRequest = request.copy(id = requestId)
            database.reference.child("blood_requests").child(requestId).setValue(newRequest).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
