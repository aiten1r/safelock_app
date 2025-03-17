package com.example.safelock.presentation.acivitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.safelock.R
import com.example.safelock.data.sharedprefences.SharedPreferencesHelper
import com.example.safelock.databinding.ActivityMainBinding
import com.example.safelock.databinding.AlertDiolgViewBinding
import com.example.safelock.presentation.viewmodel.RegistrationViewModel
import com.example.safelock.presentation.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val sharedViewModel: SharedViewModel by viewModels()
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private val registrationViewModel: RegistrationViewModel by viewModels()

    private var isPasswordVissible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferencesHelper = SharedPreferencesHelper(this)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when(navController.currentDestination?.id){
                    R.id.mainFragment -> finishAffinity()
                    R.id.loginFramgent -> finishAffinity()
                    else -> navController.navigateUp()
                }
            }
        })

        NavigationUI.setupWithNavController(binding.navigationView,navController)
        setSupportActionBar(binding.topAppBar)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.mainFragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        if (sharedPreferencesHelper.isFirstLaunch()) {
            navController.navigate(R.id.createProfile)
        } else {
            navController.navigate(R.id.loginFramgent)
        }

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.exportemailItem -> {
                    showEmailAlertDiolog()
                }

                R.id.settingsItem -> {
                    navController.navigate(R.id.settings)
                }
            }
            binding.drawerLayout.close()
            true
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.mainFragment -> {
                    binding.topAppBar.visibility = View.VISIBLE
                    binding.topAppBar.isTitleCentered = true
                    binding.topAppBar.navigationIcon =
                            //ContextCompat делает код совместимым с разными версиями андроид
                        ContextCompat.getDrawable(this, R.drawable.naviagation_drawer)
                    binding.topAppBar.menu.clear()
                    binding.topAppBar.setNavigationOnClickListener {
                        binding.drawerLayout.open()
                    }
                }

                R.id.detailsFragment -> {
                    binding.topAppBar.visibility = View.VISIBLE
                    binding.topAppBar.isTitleCentered = true
                    binding.topAppBar.navigationIcon =
                        ContextCompat.getDrawable(this, R.drawable.arrowback_icon)
                    binding.topAppBar.menu.clear()
                    binding.topAppBar.isTitleCentered = false
                    binding.topAppBar.setNavigationOnClickListener {
                        navController.navigateUp()
                    }
                }

                R.id.addPassword -> {
                    binding.topAppBar.visibility = View.VISIBLE
                    binding.topAppBar.navigationIcon = null
                    binding.topAppBar.menu.clear()
                    menuInflater.inflate(R.menu.passwordadd_menu, binding.topAppBar.menu)
                    binding.topAppBar.isTitleCentered = false
                }

                R.id.createProfile, R.id.loginFramgent, R.id.settings,R.id.changePassowrd -> {
                    binding.topAppBar.visibility = View.GONE
                }
            }
        }
    }

    private fun showEmailAlertDiolog() {
        val dialogBinding = AlertDiolgViewBinding.inflate(layoutInflater)
        AlertDialog.Builder(this)
            .setTitle("Отпровить пароль на email")
            .setView(dialogBinding.root)
            .setPositiveButton("Отправить") { _, _ ->
                val inputEmail = dialogBinding.edEmail.text.toString().trim()//удаляем лишние пробелы
                if (inputEmail.isNotEmpty()) {
                    val password = registrationViewModel.getSavedPassword()
                    Log.d("PasswordCheck", "GetedPassword: $password")
                    if (!password.isNullOrEmpty()) {
                        sendPasswordToEmail(inputEmail, password)
                    } else {
                        Toast.makeText(this, "Введите Email", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun sendPasswordToEmail(inputEmail: String, password: String) {
        val subject = "Ваш пароль"
        val messgae = "Ваш пароль: $password"

        val intetn = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(inputEmail))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, messgae)
        }
        try {
            startActivity(Intent.createChooser(intetn, "Выбирите пачтовое приложение"))
        } catch (e: Exception) {
            Toast.makeText(this, "Не удалось отправить email", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.passwordchack -> {
                isPasswordVissible = !isPasswordVissible
                updateIcon(item)
                sharedViewModel.togglePassword()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateIcon(item: MenuItem) {
        if (isPasswordVissible) {
            item.setIcon(R.drawable.showpassword_icon)
        } else {
            item.setIcon(R.drawable.passwordcheck_icon)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}

