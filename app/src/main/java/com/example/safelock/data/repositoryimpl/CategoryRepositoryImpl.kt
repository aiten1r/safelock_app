package com.example.safelock.data.repositoryimpl

import com.example.safelock.R
import com.example.safelock.data.repositry.CategoryRepository
import com.example.safelock.data.room.dao.CategoryDao
import com.example.safelock.data.room.entity.toDomainModel
import com.example.safelock.data.room.entity.toEntity
import com.example.safelock.domain.data.Category
import javax.inject.Inject

class CategoryRepositoryImpl  @Inject constructor(
    private val categoryDao: CategoryDao
): CategoryRepository {

    override suspend fun getCategories(): List<Category> {
        return categoryDao.getAllCategories().map { it.toDomainModel() }
    }

    override suspend fun updateCategoryCount(category: Category) {
        categoryDao.updateCategory(category.toEntity())
    }

    override suspend fun initializeCategories(initialCategories: List<Category>) {
        if (categoryDao.getAllCategories().isEmpty()){
            categoryDao.insertCategories(initialCategories.map { it.toEntity() })
        }
    }
}