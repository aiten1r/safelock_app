package com.example.safelock.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.safelock.domain.data.User

@Entity(tableName = "userData")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val iv: ByteArray, // IV для шифрования
    val encryptedPassword: ByteArray
)
