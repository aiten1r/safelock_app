package com.example.safelock.data.repositry

import com.example.safelock.domain.data.Category

interface CategoryRepository {
    fun getCategories():List<Category>
}