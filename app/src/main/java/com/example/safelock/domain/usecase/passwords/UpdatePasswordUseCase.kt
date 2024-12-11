package com.example.safelock.domain.usecase.passwords

import com.example.safelock.domain.repositry.PasswordRepository
import com.example.safelock.domain.data.Password
import javax.inject.Inject

class UpdatePasswordUseCase @Inject constructor(private val repository: PasswordRepository) {
    suspend operator fun invoke(password: Password){
        repository.updatePssword(password)
    }
}