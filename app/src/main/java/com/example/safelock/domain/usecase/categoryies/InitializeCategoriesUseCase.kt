package com.example.safelock.domain.usecase.categoryies

import com.example.safelock.domain.repositry.CategoryRepository
import com.example.safelock.domain.data.Category
import javax.inject.Inject

class InitializeCategoriesUseCase @Inject constructor(private val repository: CategoryRepository) {
    suspend operator fun invoke(initialCategories: List<Category>) {
        repository.initializeCategories(initialCategories)
    }
}