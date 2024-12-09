package com.example.safelock.domain.usecase.categoryies

import com.example.safelock.data.repositry.CategoryRepository
import com.example.safelock.domain.data.Category
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {
    //operator - ключевое слово в котлин который позволяеть перегрузить операторы -+* invoke()
    //метод invoke() позволяет вызывать обьект как функцию
    suspend operator fun invoke(): List<Category> = categoryRepository.getCategories()
}