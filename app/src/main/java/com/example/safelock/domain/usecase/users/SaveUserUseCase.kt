package com.example.safelock.domain.usecase.users

import com.example.safelock.domain.data.User
import com.example.safelock.domain.repositry.UserRepository
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(iv: ByteArray, encryptedPassword: ByteArray) {
        val user = User(iv = iv, encryptedPassword = encryptedPassword)
        repository.saveUser(user)
    }
}