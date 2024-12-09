package com.example.safelock.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safelock.R
import com.example.safelock.data.repositry.CategoryRepository
import com.example.safelock.domain.data.Category
import com.example.safelock.domain.data.Password
import com.example.safelock.domain.usecase.categoryies.GetCategoriesUseCase
import com.example.safelock.domain.usecase.categoryies.InitializeCategoriesUseCase
import com.example.safelock.domain.usecase.categoryies.UpdateCategoryCountUseCase
import com.example.safelock.domain.usecase.passwords.AddPasswordUseCase
import com.example.safelock.domain.usecase.passwords.DeletePasswordUseCase
import com.example.safelock.domain.usecase.passwords.GetPasswordsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val initializeCategoriesUseCase: InitializeCategoriesUseCase,
    private val updateCategoryCountUseCase: UpdateCategoryCountUseCase,
    private val getPasswordsUseCase: GetPasswordsUseCase,
    private val addPasswordUseCase: AddPasswordUseCase,
    private val deletePasswordUseCase: DeletePasswordUseCase,
) : ViewModel() {

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    private val _categoryId = MutableLiveData<Int>()

    private val _passwords = MediatorLiveData<List<Password>>()
    val passwords: LiveData<List<Password>> get() = _passwords

    init {
        _passwords.addSource(_categoryId) { categoryId ->
            loadPasswords(categoryId)
        }
    }

    fun loadCategorys(){
        viewModelScope.launch {
            _categories.value = getCategoriesUseCase()
        }

    }

    fun setCategoryId(categoryId: Int){
        _categoryId.value = categoryId
    }

    private fun loadPasswords(categoryId: Int) {
        viewModelScope.launch {
            val passwordsLiveData = getPasswordsUseCase(categoryId)
            _passwords.addSource(passwordsLiveData) { passwords ->
                _passwords.value = passwords
            }
        }
    }

    fun addPassword(password: Password) {
        viewModelScope.launch {
            addPasswordUseCase(password)
        }
    }

    fun deletePassword(password: Password) {
        viewModelScope.launch {
            deletePasswordUseCase(password)
        }
    }


    init {
        viewModelScope.launch {
            val initialCategories = listOf(
                Category(1, R.drawable.email_icon, "E-mail", 0),
                Category(2, R.drawable.wifi_icon, "Wi-Fi", 0),
                Category(3, R.drawable.websites_icon, "Веб-сайты", 0),
                Category(4, R.drawable.computer_icon, "Компьютер", 0),
                Category(5, R.drawable.socialmedia_icon, "Социальные сети", 0),
                Category(6, R.drawable.pincodes_icon, "PIN-коды", 0),
                Category(7, R.drawable.other_icon, "Прочее", 0)
            )
            initializeCategoriesUseCase(initialCategories)
            _categories.value = getCategoriesUseCase()
        }
    }

    fun updateCategoryCount(categoryId: Int) {
        viewModelScope.launch {
            val category = _categories.value?.find { it.id == categoryId }
            if (category != null) {
                val updateCategory = category.copy(count = category.count + 1)
                updateCategoryCountUseCase(updateCategory)
                _categories.value = getCategoriesUseCase()
            }
        }
    }
}
