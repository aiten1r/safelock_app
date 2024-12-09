package com.example.safelock.presentation.module

import com.example.safelock.data.repositoryimpl.CategoryRepositoryImpl
import com.example.safelock.data.repositoryimpl.PasswordRepositoryImpl
import com.example.safelock.data.repositry.CategoryRepository
import com.example.safelock.data.repositry.PasswordRepository
import com.example.safelock.data.room.appdatabase.AppDataBase
import com.example.safelock.data.room.dao.CategoryDao
import com.example.safelock.data.room.dao.PasswordDao
import com.example.safelock.domain.data.Category
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePasswordRepository(passwordDao: PasswordDao): PasswordRepository {
        return PasswordRepositoryImpl(passwordDao)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(categoryDao: CategoryDao): CategoryRepository {
        return CategoryRepositoryImpl(categoryDao)
    }
}