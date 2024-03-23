package com.waminiyi.realestatemanager.features.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.core.Constants
import com.waminiyi.realestatemanager.core.Constants.TAB_LANDSCAPE_LAYOUT_MODE
import com.waminiyi.realestatemanager.core.data.datastore.repository.UserPreferencesRepository
import com.waminiyi.realestatemanager.core.util.util.CurrencyCode
import com.waminiyi.realestatemanager.databinding.ActivityHomeBinding
import com.waminiyi.realestatemanager.features.estateListing.EstateListingViewModel
import com.waminiyi.realestatemanager.features.events.Event
import com.waminiyi.realestatemanager.features.events.EventListener
import com.waminiyi.realestatemanager.features.events.ScreenSplitListener
import com.waminiyi.realestatemanager.features.model.ListingViewType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), EventListener {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mainNavController: NavController
    private var rightNavController: NavController? = null


    private lateinit var toolbar: Toolbar
    private var currentViewType = ListingViewType.LIST
    private var splitListener: ScreenSplitListener? = null
    private var isSplit: Boolean = false
    private var isSplittable: Boolean = false


    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    private val viewModel: EstateListingViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        updateMainView(isSplit = isSplit)
        toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        setUpCurrencyButton()
        val mode = resources.getInteger(R.integer.layout_mode)
        isSplittable = mode == TAB_LANDSCAPE_LAYOUT_MODE
        if (isSplittable) {
            val rightNavHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragment_container_right) as NavHostFragment
            rightNavController = rightNavHostFragment.navController
        }

        val mainNavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_home) as NavHostFragment
        mainNavController = mainNavHostFragment.navController

        appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_estateList, R.id.estateMapFragment))
        toolbar.setupWithNavController(mainNavController, appBarConfiguration)
        mainNavController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_add, R.id.navigation_estatedetails, R.id.estateFilterFragment, R.id.loanSimulatorFragment -> {
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
            navigateToFilterFragment()
        }
        binding.loanSimulatorButton.setOnClickListener {
            navigateToLoanFragment()
        }

        binding.newEstateButton.setOnClickListener {
            navigateToEditFragment(null)
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
            mainNavController.navigate(R.id.navigation_estateList)
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
            mainNavController.navigate(R.id.estateMapFragment)
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

    private fun updateCurrencyButtonIcon(currencyCode: CurrencyCode) {
        val iconResId = if (currencyCode == CurrencyCode.EUR) {
            R.drawable.ic_euro
        } else {
            R.drawable.ic_dollars
        }
        binding.currencyButton.setImageResource(iconResId)
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.HideRightFragment -> {
                if (isSplittable) {
                    updateMainView(false)
                } else{
                    mainNavController.navigateUp()
                }
            }

            is Event.EstateClicked -> {
                navigateToDetailsFragment(event.estateId)
            }

            is Event.OpenEditFragment -> {
                navigateToEditFragment(event.estateId)
            }
        }
    }

    private fun navigateToDetailsFragment(estateUuid: String) {
        val bundle = Bundle().apply { putString(Constants.ARG_ESTATE_ID, estateUuid) }

        if (isSplittable) {
            val previousDestinationId = rightNavController?.currentDestination?.id ?: -1

            if (previousDestinationId == R.id.right_navigation_estate_details) {
                rightNavController?.navigate(
                    R.id.right_navigation_estate_details,
                    bundle,
                    NavOptions.Builder().setPopUpTo(previousDestinationId, true).build()
                )
            } else {
                rightNavController?.navigate(R.id.right_navigation_estate_details, bundle)
            }
            updateMainView(true)

        } else {
            mainNavController.navigate(R.id.navigation_estatedetails, bundle)
        }
    }

    private fun navigateToFilterFragment() {
        if (isSplittable) {
            val previousDestinationId = rightNavController?.currentDestination?.id ?: -1

            if (previousDestinationId == R.id.right_estate_filter_fragment) {
                rightNavController?.navigate(
                    R.id.right_estate_filter_fragment,
                    null,
                    NavOptions.Builder().setPopUpTo(previousDestinationId, true).build()
                )
            } else {
                rightNavController?.navigate(R.id.right_estate_filter_fragment)
            }
            updateMainView(true)

        } else {
            mainNavController.navigate(R.id.estateFilterFragment)
        }
    }

    private fun navigateToEditFragment(estateUuid: String?) {

        val bundle = Bundle().apply { putString(Constants.ARG_ESTATE_ID, estateUuid) }

        if (isSplittable) {
            val previousDestinationId = rightNavController?.currentDestination?.id ?: -1

            if (previousDestinationId == R.id.right_navigation_add) {
                rightNavController?.navigate(
                    R.id.right_navigation_add,
                    bundle,
                    NavOptions.Builder().setPopUpTo(previousDestinationId, true).build()
                )
            } else {
                rightNavController?.navigate(R.id.right_navigation_add, bundle)
            }
            updateMainView(true)
        } else {
            mainNavController.navigate(R.id.navigation_add, bundle)
        }
    }

    private fun navigateToLoanFragment() {

        if (isSplittable) {
            val previousDestinationId = rightNavController?.currentDestination?.id ?: -1

            if (previousDestinationId == R.id.right_navigation_loan_simulator) {
                rightNavController?.navigate(
                    R.id.right_navigation_loan_simulator,
                    null,
                    NavOptions.Builder().setPopUpTo(previousDestinationId, true).build()
                )
            } else {
                rightNavController?.navigate(R.id.right_navigation_loan_simulator)
            }
            updateMainView(true)
        } else {
            mainNavController.navigate(R.id.loanSimulatorFragment)
        }
    }

    fun setScreenSplitListener(listener: ScreenSplitListener) {
        this.splitListener = listener
    }

    private fun updateMainView(isSplit: Boolean = false) {
        if (isSplit) {
            binding.tabLandscapeModeRightContainer?.visibility = View.VISIBLE
            splitListener?.onScreenSplittingChanged(true)
        } else {
            binding.tabLandscapeModeRightContainer?.visibility = View.GONE
            splitListener?.onScreenSplittingChanged(false)
        }
    }
}