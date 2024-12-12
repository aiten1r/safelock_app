package com.example.safelock.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.safelock.data.room.entity.UserEntity

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM userData LIMIT 1")
    suspend fun getUser(): UserEntity

    @Update
    suspend fun updateUser(user: UserEntity)

}