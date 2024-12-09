package com.example.safelock.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.safelock.domain.data.Category

@Entity (tableName = "categories")
data class CategoryEntity (
    @PrimaryKey (autoGenerate = true) val id : Int,
    val iconRes: Int,
    val title: String,
    var count: Int
)
fun CategoryEntity.toDomainModel() = Category(id, iconRes, title, count)
fun Category.toEntity() = CategoryEntity(id, iconRes, title, count)