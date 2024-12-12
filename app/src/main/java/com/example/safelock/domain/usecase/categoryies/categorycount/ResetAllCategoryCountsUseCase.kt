package com.example.safelock.domain.usecase.categoryies.categorycount

import com.example.safelock.domain.repositry.CategoryRepository
import javax.inject.Inject

class ResetAllCategoryCountsUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(){
        val allCategories = categoryRepository.getCategories()
        allCategories.forEach { category ->
            val updatedCategory = category.copy(count = 0)
            categoryRepository.updateCategoryCount(updatedCategory)
        }
    }
}