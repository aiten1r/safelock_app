package com.example.safelock.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "password_table")
data class PasswordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val password: String,
    val description: String,
    val categoryId: Int
    )
