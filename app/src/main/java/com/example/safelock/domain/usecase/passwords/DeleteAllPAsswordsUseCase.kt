package com.example.safelock.domain.usecase.passwords

import com.example.safelock.domain.repositry.PasswordRepository
import javax.inject.Inject

class DeleteAllPAsswordsUseCase @Inject constructor(
    private val repository: PasswordRepository
) {
    suspend operator fun invoke(){
        repository.deletAllPasswords()
    }
}