package com.example.safelock.presentation.acivitys

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.safelock.R
import com.example.safelock.databinding.ActivityMainBinding

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
        setupActionBarWithNavController(navController,appBarConfiguration)

        navController.addOnDestinationChangedListener{_,destination,_->
            if (destination.id == R.id.mainFragment){
                binding.topAppBar.navigationIcon=
                    ContextCompat.getDrawable(this,R.drawable.naviagation_drawer)
                binding.topAppBar.setNavigationOnClickListener{
                    Toast.makeText(this, "Drawer icon clicked", Toast.LENGTH_SHORT).show()
                }
            }else{
                binding.topAppBar.navigationIcon=
                    ContextCompat.getDrawable(this,R.drawable.arrowback_icon)
                binding.topAppBar.setNavigationOnClickListener{
                    navController.navigateUp()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
