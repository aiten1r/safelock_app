package com.example.safelock.presentation.acivitys

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.safelock.R
import com.example.safelock.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

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
                    binding.topAppBar.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.topAppBarColor
                        )
                    )
                    binding.topAppBar.navigationIcon =
                            //ContextCompat делает код совместимым с разными версиями андроид
                        ContextCompat.getDrawable(this, R.drawable.naviagation_drawer)
                    binding.topAppBar.menu.clear()
                    binding.topAppBar.setNavigationOnClickListener{
                        binding.drawerLayout.open()
                    }
                }

                R.id.detailsFragment -> {
                    binding.topAppBar.isTitleCentered = true
                    binding.topAppBar.navigationIcon =
                        ContextCompat.getDrawable(this, R.drawable.arrowback_icon)
                    binding.topAppBar.menu.clear()
                    menuInflater.inflate(R.menu.topappbar_menu, binding.topAppBar.menu)
                    binding.topAppBar.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.topAppBarColor
                        )
                    )
                    binding.topAppBar.setNavigationOnClickListener {
                        navController.navigateUp()
                    }
                }

                R.id.addPassword -> {
                    binding.topAppBar.navigationIcon = null
                    binding.topAppBar.menu.clear()
                    menuInflater.inflate(R.menu.passwordadd_menu, binding.topAppBar.menu)
                    binding.topAppBar.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.topAppBarColor                        )
                    )
                    binding.topAppBar.isTitleCentered = false
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}

