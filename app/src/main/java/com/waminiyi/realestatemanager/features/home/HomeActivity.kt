package com.waminiyi.realestatemanager.features.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.core.data.datastore.repository.UserPreferencesRepository
import com.waminiyi.realestatemanager.core.util.util.CurrencyCode
import com.waminiyi.realestatemanager.databinding.ActivityHomeBinding
import com.waminiyi.realestatemanager.features.estateListing.EstateListingViewModel
import com.waminiyi.realestatemanager.features.model.ListingViewType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var toolbar: Toolbar
    private var currentViewType = ListingViewType.LIST


    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    private val viewModel: EstateListingViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("viewmodel", viewModel.toString())

        toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        setUpCurrencyButton()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_home) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_estateList, R.id.estateMapFragment))
        toolbar.setupWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_add, R.id.navigation_estatedetails -> {
                    toolbar.visibility = View.GONE
                    binding.listViewControlsLayout.visibility = View.GONE
                    binding.newEstateButton.visibility = View.GONE
                }

                else -> {
                    toolbar.visibility = View.VISIBLE
                    if (destination.id == R.id.navigation_estateList || destination.id == R.id.estateMapFragment) {
                        binding.newEstateButton.visibility = View.VISIBLE
                        binding.listViewControlsLayout.visibility = View.VISIBLE
                        if (destination.id == R.id.navigation_estateList) {
                            viewModel.updateCurrentViewType(ListingViewType.LIST)
                        } else viewModel.updateCurrentViewType(ListingViewType.MAP)
                    } else {
                        binding.newEstateButton.visibility = View.GONE
                        binding.listViewControlsLayout.visibility = View.GONE
                    }
                }
            }
        }
        binding.filterButton.setOnClickListener {
            navController.navigate(R.id.estateFilterFragment)
        }
        binding.newEstateButton.setOnClickListener {
            navController.navigate(R.id.navigation_add)
        }
        binding.listViewLabelTextView.setOnClickListener {
            viewModel.updateCurrentViewType(ListingViewType.LIST)
        }
        binding.mapViewLabelTextView.setOnClickListener {
            viewModel.updateCurrentViewType(ListingViewType.MAP)
        }
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                updateCurrencyButtonIcon(uiState.currencyCode)
                updateFilterButton(uiState.hasFilter)

                when (uiState.viewType) {
                    ListingViewType.LIST -> showListView(uiState.estates.size)

                    ListingViewType.MAP -> showMapView(uiState.estates.size)
                }
                Log.d("filter", uiState.hasFilter.toString())
            }
        }
    }

    private fun updateFilterButton(hasFilter: Boolean) {
        val color = if (hasFilter) R.color.cinnabar else R.color.black
        val icon = if (hasFilter) R.drawable.ic_filter else R.drawable.ic_filter_off
        val filterLabel = if (hasFilter) "Filtered" else "No filter"
        val tint = ContextCompat.getColor(this, color)

        binding.filterButton.setImageDrawable(ContextCompat.getDrawable(this, icon))
        binding.filterButton.setColorFilter(tint)
        binding.filterLabelTextView.text = filterLabel
        binding.filterLabelTextView.setTextColor(tint)
    }

    private fun TextView.showAsCurrentListingViewLabel(isCurrentView: Boolean) {
        val regular = ResourcesCompat.getFont(context, R.font.source_sans_regular)
        val bold = ResourcesCompat.getFont(context, R.font.source_sans_bold)
        if (isCurrentView) {
            with(this) {
                setTextColor(ContextCompat.getColor(this.context, R.color.cinnabar))
                typeface = bold
            }
        } else {
            with(this) {
                setTextColor(ContextCompat.getColor(this.context, R.color.black))
                typeface = regular
            }
        }
    }

    private fun showListView(count: Int) {
        if (currentViewType != ListingViewType.LIST) {
            currentViewType = ListingViewType.LIST
            navController.navigate(R.id.navigation_estateList)
        }
        val listCountText = getString(R.string.list_view_label, count)
        val mapCountText = getString(R.string.map_view_label, count)
        binding.listViewLabelTextView.text = listCountText
        binding.mapViewLabelTextView.text = mapCountText
        binding.listViewLabelTextView.showAsCurrentListingViewLabel(true)
        binding.mapViewLabelTextView.showAsCurrentListingViewLabel(false)
    }

    private fun showMapView(count: Int) {
        if (currentViewType != ListingViewType.MAP) {
            currentViewType = ListingViewType.MAP
            navController.navigate(R.id.estateMapFragment)
        }
        val listCountText = getString(R.string.list_view_label, count)
        val mapCountText = getString(R.string.map_view_label, count)
        binding.listViewLabelTextView.text = listCountText
        binding.mapViewLabelTextView.text = mapCountText
        binding.listViewLabelTextView.showAsCurrentListingViewLabel(false)
        binding.mapViewLabelTextView.showAsCurrentListingViewLabel(true)
    }

    private fun setUpCurrencyButton() {

        binding.currencyButton.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)
            popupMenu.menuInflater.inflate(R.menu.currency_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.currency_dollars -> {
                        viewModel.updateCurrencyCode(CurrencyCode.USD)
                        updateCurrencyButtonIcon(CurrencyCode.USD)
                        true
                    }

                    R.id.currency_euro -> {
                        viewModel.updateCurrencyCode(CurrencyCode.EUR)
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

    private fun updateCurrencyButtonIcon(currencyCode: CurrencyCode) {
        val iconResId = if (currencyCode == CurrencyCode.EUR) {
            R.drawable.ic_euro
        } else {
            R.drawable.ic_dollars
        }
        binding.currencyButton.setImageResource(iconResId)
    }
}