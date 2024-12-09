package com.example.safelock.domain.usecase.categoryies

import com.example.safelock.data.repositry.CategoryRepository
import com.example.safelock.domain.data.Category
import javax.inject.Inject

class UpdateCategoryCountUseCase @Inject constructor(private val repository: CategoryRepository) {
    suspend operator fun invoke(category: Category){
        repository.updateCategoryCount(category)
    }
}