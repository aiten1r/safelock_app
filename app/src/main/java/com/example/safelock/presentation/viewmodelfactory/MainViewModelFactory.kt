package com.example.safelock.presentation.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.safelock.domain.usecase.GetCategoriesUseCase
import com.example.safelock.presentation.viewmodel.MainViewModel

class MainViewModelFactory(private val getCategoriesUseCase: GetCategoriesUseCase):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(getCategoriesUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}