package com.example.safelock.domain.usecase.users

import com.example.safelock.domain.repositry.UserRepository
import com.example.safelock.data.util.CryptoUtil
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun execute(oldPassword: String, newPassword: String): Boolean {
        val user = userRepository.getUser() ?: return false

        val decryptedPassword = CryptoUtil.decrypt(user.iv, user.encryptedPassword)
        if (decryptedPassword != oldPassword) {
            return false // Старый пароль неверный
        }

        val (iv, encryptedPassword) = CryptoUtil.encrypt(newPassword)
        userRepository.updatePassword(iv, encryptedPassword)
        return true // Пароль успешно изменён
    }
}