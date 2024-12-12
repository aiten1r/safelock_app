package com.example.safelock.domain.repositry

import com.example.safelock.data.room.entity.UserEntity
import com.example.safelock.domain.data.User

interface UserRepository {
    suspend fun saveUser(user: User)
    suspend fun getUser(): User?
    suspend fun updatePassword(iv:ByteArray,encryptedPassword:ByteArray)
}