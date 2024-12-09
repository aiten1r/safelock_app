package com.example.safelock.domain.usecase.passwords

import com.example.safelock.data.repositry.PasswordRepository
import com.example.safelock.domain.data.Password
import javax.inject.Inject

class DeletePasswordUseCase @Inject constructor(private val repository: PasswordRepository) {
    suspend operator fun invoke(password: Password){
        repository.deletePassword(password)
    }
}