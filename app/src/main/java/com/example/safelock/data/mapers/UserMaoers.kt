package com.example.safelock.data.mapers

import com.example.safelock.data.room.entity.UserEntity
import com.example.safelock.domain.data.User


fun UserEntity.toDomain(): User {
    return User(id = this.id, iv = this.iv, encryptedPassword = this.encryptedPassword)
}

fun User.toData(): UserEntity {
    return UserEntity(id = this.id, iv = this.iv, encryptedPassword = this.encryptedPassword)
}