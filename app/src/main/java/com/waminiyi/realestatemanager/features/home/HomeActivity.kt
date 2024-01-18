package com.waminiyi.realestatemanager.features.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.databinding.ActivityHomeBinding
import com.waminiyi.realestatemanager.features.editestate.EditEstateFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_home) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)

        // Add menu items without overriding methods in the Activity
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.appbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.navigation_add -> {
                        // Handle the click on the "Add" menu item
                        // Example: navigate to the AddFragment
                        navigateToAddFragment()
                        return true
                    }

                    R.id.navigation_edit -> {
                        // Handle the click on the "Edit" menu item
                        // Example: navigate to the EditFragment
                        showDialog("edit")
                        navigateToEditFragment()
                        return true
                    }

                    R.id.navigation_filter -> {
                        // Handle the click on the "Filter" menu item
                        // Example: show a filter dialog
                        showDialog("Filter")
                        return true
                    }

                    R.id.currency_dollars -> {
                        // Handle the click on the "Dollars" sub-menu item
                        // Example: set the currency to Dollars
                        setCurrencyToDollars()
                        return true
                    }

                    R.id.currency_euro -> {
                        // Handle the click on the "Euro" sub-menu item
                        // Example: set the currency to Euro
                        setCurrencyToEuro()
                        return true
                    }

                    R.id.navigation_settings -> {
                        // Handle the click on the "Settings" menu item
                        // Example: navigate to the SettingsFragment
                        navigateToSettingsFragment()
                        return true
                    }

                    else -> return true
                }
            }
        })
    }

    private fun navigateToAddFragment() {
        navController.navigate(R.id.navigation_add)
    }

    private fun navigateToEditFragment() {

        val fragmentManager = supportFragmentManager
        val fragment = EditEstateFragment.newInstance(null)
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment_activity_home, fragment)
            .commit()
    }

    private fun showDialog(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    private fun setCurrencyToDollars() {
// Implement logic for setting currency to Dollars
    }

    private fun setCurrencyToEuro() {
// Implement logic for setting currency to Euro
    }

    private fun navigateToSettingsFragment() {
// Implement your navigation logic here
    }
}