package com.example.safelock.data.repositoryimpl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.safelock.data.room.dao.PasswordDao
import com.example.safelock.data.room.entity.PasswordEntity
import com.example.safelock.data.repositry.PasswordRepository
import com.example.safelock.domain.data.Password
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PasswordRepositoryImpl(private val passwordDao: PasswordDao) : PasswordRepository {

    override fun getAllPassword(): LiveData<List<Password>> {
        val result = MediatorLiveData<List<Password>>()
        result.addSource(passwordDao.getAllPassword()) { entities ->
            result.value = entities.map { it.toDomain() } // Вызов метода маппинга
        }
        return result
    }

    override suspend fun getPasswordsByCategory(categoryId: Int): LiveData<List<Password>> {
        val result = MediatorLiveData<List<Password>>()
        result.addSource(passwordDao.getPasswordsByCategory(categoryId)) { entities ->
            result.value = entities.map { it.toDomain() }
        }
        return result
    }

    override suspend fun addPassword(password: Password) {
        withContext(Dispatchers.IO) {
            passwordDao.insertPassword(password.toEntity()) // Вызов метода маппинга
        }
    }

    override suspend fun deletePassword(password: Password) {
        withContext(Dispatchers.IO) {
            passwordDao.deletePassword(password.toEntity())
        }
    }


    // Мапперы для преоброзавние entity в data class для слоя domain
    fun PasswordEntity.toDomain(): Password {
        return Password(id, title, password, description, categoryId)
    }

    fun Password.toEntity(): PasswordEntity {
        return PasswordEntity(id, title, password, description, categoryId )
    }
}
