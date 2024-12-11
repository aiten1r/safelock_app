package com.example.safelock.domain.usecase.categoryies.categorycount

import com.example.safelock.domain.repositry.CategoryRepository
import javax.inject.Inject

class IncrementCategoryCountUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(categoryId: Int) {
        val category = categoryRepository.getCategories().find { it.id == categoryId }
        if (category != null) {
            val updatedCategory = category.copy(count = category.count + 1)
            categoryRepository.updateCategoryCount(updatedCategory)
        }
    }
}