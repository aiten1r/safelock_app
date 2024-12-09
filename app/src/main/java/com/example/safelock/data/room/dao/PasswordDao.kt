package com.example.safelock.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.safelock.data.room.entity.PasswordEntity
import com.example.safelock.domain.data.Password

@Dao
interface PasswordDao {
    @Insert
    suspend fun insertPassword(passwordEntity: PasswordEntity)

    @Delete
    suspend fun deletePassword(passwordEntity: PasswordEntity)

    @Query ("SELECT * FROM password_table ORDER BY id ASC")
    fun getAllPassword(): LiveData<List<PasswordEntity>>

    @Query ("SELECT * FROM password_table WHERE categoryId = :categoryId ORDER BY id ASC")
    fun getPasswordsByCategory(categoryId:Int):LiveData<List<PasswordEntity>>

}