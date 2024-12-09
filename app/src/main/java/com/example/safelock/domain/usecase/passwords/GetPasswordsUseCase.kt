package com.example.safelock.domain.usecase.passwords

import androidx.lifecycle.LiveData
import com.example.safelock.data.repositry.PasswordRepository
import com.example.safelock.domain.data.Password
import javax.inject.Inject

class GetPasswordsUseCase @Inject constructor(private val repository: PasswordRepository) {
     suspend operator fun invoke(categoryId: Int): LiveData<List<Password>>{
        return repository.getPasswordsByCategory(categoryId)
    }
}