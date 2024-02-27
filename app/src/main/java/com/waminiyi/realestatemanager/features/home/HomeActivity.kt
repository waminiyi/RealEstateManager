package com.waminiyi.realestatemanager.features.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.core.data.datastore.repository.UserPreferencesRepository
import com.waminiyi.realestatemanager.core.util.util.CurrencyCode
import com.waminiyi.realestatemanager.databinding.ActivityHomeBinding
import com.waminiyi.realestatemanager.features.editestate.EditEstateFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var toolbar: Toolbar

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        setUpCurrencyButton()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_home) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_add -> {
                    toolbar.visibility = View.GONE
                }

                R.id.navigation_estatedetails -> {
                    toolbar.visibility = View.GONE
                }

                else -> {
                    toolbar.visibility = View.VISIBLE
                    binding.currencyButton.visibility = View.VISIBLE
                }

            }
        }

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.appbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.navigation_add -> {
                        navController.navigate(R.id.navigation_add)
                        return true
                    }

                    R.id.navigation_filter -> {
                        showDialog("Filter")
                        return true
                    }

                    R.id.navigation_settings -> {
                        navigateToSettingsFragment()
                        return true
                    }

                    R.id.navigation_agents -> {
                        navController.navigate(R.id.navigation_agents_list)
                        return true
                    }

                    else -> return false
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

    private fun setUpCurrencyButton() {
        lifecycleScope.launch {
            userPreferencesRepository.getDefaultCurrency().collect { code ->
                updateCurrencyButtonIcon(code)
            }
        }

        binding.currencyButton.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)
            popupMenu.menuInflater.inflate(R.menu.currency_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.currency_dollars -> {
                        setCurrencyToDollars()
                        updateCurrencyButtonIcon(CurrencyCode.USD)
                        true
                    }

                    R.id.currency_euro -> {
                        setCurrencyToEuro()
                        updateCurrencyButtonIcon(CurrencyCode.EUR)

                        true
                    }

                    else -> false
                }
            }

            popupMenu.show()
        }
    }

    private fun showDialog(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    private fun setCurrencyToDollars() {
        this.lifecycleScope.launch {
            userPreferencesRepository.updateDefaultCurrency(CurrencyCode.USD)
        }
    }

    private fun setCurrencyToEuro() {
        this.lifecycleScope.launch {
            userPreferencesRepository.updateDefaultCurrency(CurrencyCode.EUR)
        }
    }

    private fun navigateToSettingsFragment() {
// Implement your navigation logic here
    }

    private fun updateCurrencyButtonIcon(currencyCode: CurrencyCode) {
        val iconResId = if (currencyCode == CurrencyCode.EUR) {
            R.drawable.ic_euro
        } else {
            R.drawable.ic_dollars
        }
        binding.currencyButton.setImageResource(iconResId)
    }
}