package com.example.blooddonor.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.blooddonor.data.dao.UserDao
import com.example.blooddonor.data.models.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
