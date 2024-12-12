package com.example.safelock.domain.usecase.users

import com.example.safelock.domain.repositry.UserRepository
import com.example.safelock.data.util.CryptoUtil
import javax.inject.Inject

class CheckPasswordUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(inputPassword:String):Boolean{
        val user = repository.getUser() ?: return false
        val descryptedPassword = CryptoUtil.decrypt(user.iv, user.encryptedPassword)
        return descryptedPassword == inputPassword
    }
}