package com.example.safelock.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.safelock.R
import com.example.safelock.domain.data.Category
import com.example.safelock.domain.data.Password
import com.example.safelock.domain.usecase.categoryies.GetCategoriesUseCase
import com.example.safelock.domain.usecase.categoryies.InitializeCategoriesUseCase
import com.example.safelock.domain.usecase.categoryies.categorycount.DecrementCategoryCountUseCase
import com.example.safelock.domain.usecase.categoryies.categorycount.IncrementCategoryCountUseCase
import com.example.safelock.domain.usecase.passwords.AddPasswordUseCase
import com.example.safelock.domain.usecase.passwords.DeleteAllPAsswordsUseCase
import com.example.safelock.domain.usecase.passwords.DeletePasswordUseCase
import com.example.safelock.domain.usecase.passwords.GetPasswordsUseCase
import com.example.safelock.domain.usecase.passwords.UpdatePasswordUseCase
import com.example.safelock.domain.usecase.users.ChangePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val initializeCategoriesUseCase: InitializeCategoriesUseCase,
    private val getPasswordsUseCase: GetPasswordsUseCase,
    private val addPasswordUseCase: AddPasswordUseCase,
    private val deletePasswordUseCase: DeletePasswordUseCase,
    private val incrementCategoryCountUseCase: IncrementCategoryCountUseCase,
    private val decrementCategoryCountUseCase: DecrementCategoryCountUseCase,
    private val updatePasswordUseCase: UpdatePasswordUseCase,
    private val deletAllPAsswords: DeleteAllPAsswordsUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase
) : ViewModel() {

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    private val _categoryId = MutableLiveData<Int>()

    private val _passwords = MediatorLiveData<List<Password>>()
    val passwords: LiveData<List<Password>> get() = _passwords
    private var currentPasswordSource: LiveData<List<Password>>? = null

    private val _togglePasswordVisibility = MutableLiveData<Unit>()
    val togglePasswordVisibility: LiveData<Unit> get() = _togglePasswordVisibility

    fun deletAllPasswords(){
        viewModelScope.launch {
            try {
                deletAllPAsswords()
            }catch (e:Exception){
                Log.e("SharedViewModel", "Ошибка удаления паролей: ${e.message}")
            }
        }
    }

    //очищает список паролей установив пустой спискок
    fun clearPassword() {
        _passwords.value = emptyList()
    }

    fun togglePassword() {
        _togglePasswordVisibility.value = Unit
    }

    //звгружает все категорий из базы данных через getCategoriesUseCase()
    fun loadCategorys() {
        viewModelScope.launch {
            _categories.value = getCategoriesUseCase()
        }

    }

    fun setCategoryId(categoryId: Int) {
        Log.d("SharedViewModel", "setCategoryId called with: $categoryId")
        _categoryId.value = categoryId
    }

    fun addPassword(password: Password, categoryId: Int) {
        viewModelScope.launch {
            addPasswordUseCase(password) //добавляет новый пароль в базу данных
            incrementCategoryCountUseCase(password.categoryId) //увеличивает счетчик в категорий
        }
    }

    fun deletePassword(password: Password) {
        viewModelScope.launch {
            deletePasswordUseCase(password)//удаляет пароль из базы двнных
            decrementCategoryCountUseCase(password.categoryId)//уменьшает счетчки в категорий
        }
    }

    fun updatePassword(password: Password) {
        viewModelScope.launch {
            updatePasswordUseCase(password)// обновляет данные сществуещего пароля
        }
    }
    suspend fun changePassword(oldPassword:String,newPassword:String):Boolean{
        return changePasswordUseCase.execute(oldPassword, newPassword)
    }

    init {
        _passwords.addSource(_categoryId) { categoryId ->
            loadPasswords(categoryId)
            Log.d("DetailsFragment", "CategoryId in DetailsFragment: $categoryId")
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

    private fun loadPasswords(categoryId: Int) {
        Log.d("SharedViewModel", "Loading passwords for categoryId: $categoryId")
        viewModelScope.launch {
            val passwordsLiveData = getPasswordsUseCase(categoryId)
            _passwords.apply {
                currentPasswordSource?.let { removeSource(it) }
                currentPasswordSource = passwordsLiveData
                addSource(passwordsLiveData) { passwords ->
                    value = passwords
                    Log.d("SharedViewModel", "Loaded passwords for categoryId: $categoryId")
                }
            }
        }
    }
}
