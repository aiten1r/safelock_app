package com.example.safelock.presentation.module

import android.content.Context
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.safelock.data.repositoryimpl.CategoryRepositoryImpl
import com.example.safelock.data.room.appdatabase.AppDataBase
import com.example.safelock.data.room.dao.PasswordDao
import com.example.safelock.data.repositoryimpl.PasswordRepositoryImpl
import com.example.safelock.data.repositry.CategoryRepository
import com.example.safelock.data.repositry.PasswordRepository
import com.example.safelock.data.room.dao.CategoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE password_table ADD COLUMN categoryId INTEGER NOT NULL DEFAULT 0")
        }
    }
    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Создание таблицы categories
            database.execSQL(
                """
            CREATE TABLE IF NOT EXISTS categories (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                iconRes INTEGER NOT NULL,
                title TEXT NOT NULL,
                count INTEGER NOT NULL
            )
            """.trimIndent()
            )
        }
    }


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDataBase {
        return Room.databaseBuilder(
            context,
            AppDataBase::class.java,
            "password_database"
        ).addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .build()
    }

    @Provides
    fun providePasswordDao(dataBase: AppDataBase): PasswordDao = dataBase.passwordDao()

    @Provides
    fun provideCategoryDao(dataBase: AppDataBase):CategoryDao = dataBase.categoryDao()


}