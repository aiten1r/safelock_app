package com.example.safelock.domain.data

data class User(
    val id: Int = 0,
    val iv: ByteArray,
    val encryptedPassword: ByteArray
)
