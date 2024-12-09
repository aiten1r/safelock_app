package com.example.safelock.data.room.appdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.safelock.data.room.dao.CategoryDao
import com.example.safelock.data.room.dao.PasswordDao
import com.example.safelock.data.room.entity.CategoryEntity
import com.example.safelock.data.room.entity.PasswordEntity

@Database(entities = [PasswordEntity::class,CategoryEntity::class], version = 3, exportSchema = false)
abstract class AppDataBase:RoomDatabase(){
    abstract fun passwordDao(): PasswordDao
    abstract fun categoryDao(): CategoryDao
}
