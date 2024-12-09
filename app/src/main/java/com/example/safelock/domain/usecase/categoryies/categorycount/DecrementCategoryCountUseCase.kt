package com.example.safelock.domain.usecase.categoryies.categorycount

import com.example.safelock.data.repositry.CategoryRepository
import javax.inject.Inject

class DecrementCategoryCountUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(categoryId: Int) {
        val category = categoryRepository.getCategories().find { it.id == categoryId }
        if (category != null && category.count > 0) { // Уменьшаем только если count > 0
            val updatedCategory = category.copy(count = category.count - 1)
            categoryRepository.updateCategoryCount(updatedCategory)
        }
    }
}