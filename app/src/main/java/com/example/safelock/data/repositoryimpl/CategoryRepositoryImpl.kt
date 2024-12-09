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


    //    override fun getCategories(): List<Category> {
//        return listOf(
//            Category(1, R.drawable.email_icon, "E-mail", 0),
//            Category(2, R.drawable.wifi_icon, "Wi-Fi", 0),
//            Category(3, R.drawable.websites_icon, "Веб-сайты", 0),
//            Category(4, R.drawable.computer_icon, "Компьютер", 0),
//            Category(5, R.drawable.socialmedia_icon, "Социальные сети", 0),
//            Category(6, R.drawable.pincodes_icon, "PIN-коды", 0),
//            Category(7, R.drawable.other_icon, "Прочее", 0)
//        )
//    }
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