package com.example.safelock.data.repositoryimpl

import com.example.safelock.data.mapers.toData
import com.example.safelock.data.mapers.toDomain
import com.example.safelock.data.room.dao.UserDao
import com.example.safelock.data.room.entity.UserEntity
import com.example.safelock.domain.data.User
import com.example.safelock.domain.repositry.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {
    override suspend fun saveUser(user: User) {
        val userEntity = user.toData()
        userDao.insertUser(userEntity)
    }

    override suspend fun getUser(): User? {
        val userEntity = userDao.getUser()
        return userEntity?.toDomain()
    }
}