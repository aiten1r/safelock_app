package com.example.safelock.domain.data


data class Password(
    val id: Int,
    val title: String,
    val password: String,
    val description: String,
    val categoryId: Int
)
