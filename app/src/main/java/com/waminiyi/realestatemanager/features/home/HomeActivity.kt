package com.waminiyi.realestatemanager.features.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*        val toolbar: Toolbar = binding.toolbar
                setSupportActionBar(toolbar)

                navController = findNavController(R.id.nav_host_fragment_activity_home)*/
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*        val appBarConfiguration = AppBarConfiguration(
                    setOf(
                        R.id.navigation_add, R.id.navigation_edit, R.id.navigation_filter
                    )
                )
                setupActionBarWithNavController(navController, appBarConfiguration)
                toolbar.setupWithNavController(navController)*/
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        return navController.navigateUp()
                || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_menu, menu)
        return true
    }

   override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle your menu item clicks here
        return super.onOptionsItemSelected(item)
    }


}