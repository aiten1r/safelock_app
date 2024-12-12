package com.example.safelock.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safelock.domain.data.User
import com.example.safelock.domain.usecase.users.CheckPasswordUseCase
import com.example.safelock.domain.usecase.users.SaveUserUseCase
import com.example.safelock.data.util.CryptoUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val saveUserUseCase: SaveUserUseCase,
    private val checkPasswordUseCase: CheckPasswordUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    // Состояние для управления UI
    private val _uiState = MutableStateFlow<String>("")
    val uiState = _uiState.asStateFlow()

    // Регистрация пользователя
    fun registerUser(password: String, confirmPassword: String) {
        if (password.isEmpty() || confirmPassword.isEmpty()) {
            _uiState.value = "Заполните все поля!"
            return
        }

        if (password != confirmPassword) {
            _uiState.value = "Пароли не совпадают!"
            return
        }

        viewModelScope.launch {
            try {
                // Шифрование пароля
                val (iv, encryptedPassword) = CryptoUtil.encrypt(password)

                // Сохранение пользователя
                saveUserUseCase(iv = iv, encryptedPassword = encryptedPassword)
                savedStateHandle["password"] = password
                _uiState.value = "Профиль создан!"
            } catch (e: Exception) {
                _uiState.value = "Ошибка при сохранении пользователя: ${e.message}"
            }
        }
    }

    // Проверка пароля
    fun login(inputPassword: String) {
        if (inputPassword.isEmpty()) {
            _uiState.value = "Введите пароль!"
            return
        }

        viewModelScope.launch {
            try {
                // Проверка пароля через UseCase
                val isValid = checkPasswordUseCase(inputPassword)

                // Обновление состояния в зависимости от результата
                _uiState.value = if (isValid) "Успешный вход!" else "Неверный пароль!"
            } catch (e: Exception) {
                _uiState.value = "Ошибка при входе: ${e.message}"
            }
        }
    }

    fun getSavedPassword(): String? {
        return savedStateHandle["password"]
    }
}