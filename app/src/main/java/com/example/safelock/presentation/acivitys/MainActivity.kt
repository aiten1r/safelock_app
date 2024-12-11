package com.example.safelock.presentation.acivitys

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.safelock.R
import com.example.safelock.databinding.ActivityMainBinding
import com.example.safelock.presentation.fragments.DetailsFragment
import com.example.safelock.presentation.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val sharedViewModel: SharedViewModel by viewModels()

    private var isPasswordVissible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setSupportActionBar(binding.topAppBar)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.mainFragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.mainFragment -> {
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
                    binding.topAppBar.navigationIcon = null
                    binding.topAppBar.menu.clear()
                    menuInflater.inflate(R.menu.passwordadd_menu, binding.topAppBar.menu)
                    binding.topAppBar.isTitleCentered = false
                }
            }
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
        if (isPasswordVissible){
            item.setIcon(R.drawable.showpassword_icon)
        }else{
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

