package com.example.blooddonor.data.repository

import com.example.blooddonor.data.dao.UserDao
import com.example.blooddonor.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userDao: UserDao
) {

    suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user!!
            // Fetch user data from Firestore and save to Room
            val documentSnapshot = firestore.collection("users").document(user.uid).get().await()
            val userData = documentSnapshot.toObject(User::class.java)
            userData?.let { userDao.insertOrUpdateUser(it) }
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(email: String, password: String, userData: User): Result<FirebaseUser> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: throw Exception("User creation failed")

            // Save user data to Firestore
            val updatedUser = userData.copy(id = userId)
            firestore.collection("users").document(userId).set(updatedUser).await()

            // Save user to Room
            userDao.insertOrUpdateUser(updatedUser)

            Result.success(authResult.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun logout() {
        auth.signOut()
        // Consider clearing local data on logout
        // GlobalScope.launch { userDao.deleteAllUsers() }
    }
}