package com.example.safelock.domain.repositry

import com.example.safelock.domain.data.Category

interface CategoryRepository {
    suspend fun getCategories():List<Category>
    suspend fun updateCategoryCount(category: Category)
    suspend fun initializeCategories(initialCategories: List<Category>)
}