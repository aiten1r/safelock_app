package com.example.safelock.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.safelock.domain.data.Category
import com.example.safelock.domain.usecase.GetCategoriesUseCase

class MainViewModel(private val getCategoriesUseCase: GetCategoriesUseCase):ViewModel() {
    private val _categorys = MutableLiveData<List<Category>>()
    val categorys:LiveData<List<Category>> get() = _categorys

    fun loadCategorys(){
        _categorys.value = getCategoriesUseCase()
    }
}