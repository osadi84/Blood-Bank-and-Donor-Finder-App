package com.example.blooddonor.data.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.PropertyName

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    @PropertyName("id")
    val id: String = "",

    @PropertyName("name")
    val name: String = "",

    @PropertyName("email")
    val email: String = "",

    @PropertyName("phone")
    val phone: String = "",

    @PropertyName("bloodType")
    val bloodType: String = "",

    @PropertyName("city")
    val city: String = "",

    @PropertyName("latitude")
    val latitude: Double = 0.0,

    @PropertyName("longitude")
    val longitude: Double = 0.0,

    @PropertyName("lastDonation")
    val lastDonation: String = "",

    @PropertyName("isAvailable")
    val isAvailable: Boolean = true,

    @PropertyName("profileImageUrl")
    val profileImageUrl: String = "",

    @PropertyName("createdAt")
    val createdAt: Long = System.currentTimeMillis()
)

data class BloodRequest(
    val id: String = "",
    val patientName: String = "",
    val bloodType: String = "",
    val hospital: String = "",
    val units: Int = 1,
    val urgency: String = "Normal", // High, Medium, Low
    val contact: String = "",
    val location: String = "",
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val userId: String = ""
)