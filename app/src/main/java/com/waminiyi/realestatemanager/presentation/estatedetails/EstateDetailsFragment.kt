package com.waminiyi.realestatemanager.presentation.estatedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import coil.load
import coil.transform.CircleCropTransformation
import com.waminiyi.realestatemanager.BuildConfig
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.Constants
import com.waminiyi.realestatemanager.Constants.STATIC_MAP_BASE_URL
import com.waminiyi.realestatemanager.Constants.STATIC_MAP_MARKER_COLOR
import com.waminiyi.realestatemanager.Constants.STATIC_MAP_SCALE
import com.waminiyi.realestatemanager.Constants.STATIC_MAP_ZOOM
import com.waminiyi.realestatemanager.data.models.EstateStatus
import com.waminiyi.realestatemanager.data.models.EstateWithDetails
import com.waminiyi.realestatemanager.data.models.Location
import com.waminiyi.realestatemanager.data.models.toRawString
import com.waminiyi.realestatemanager.util.util.CurrencyCode
import com.waminiyi.realestatemanager.util.util.getFormattedDate
import com.waminiyi.realestatemanager.util.util.priceToText
import com.waminiyi.realestatemanager.databinding.FragmentEstateDetailsBinding
import com.waminiyi.realestatemanager.events.Event
import com.waminiyi.realestatemanager.events.EventListener
import com.waminiyi.realestatemanager.presentation.estatedetails.adapters.PhotoAdapter
import com.waminiyi.realestatemanager.presentation.estatedetails.adapters.PoiAdapter
import com.waminiyi.realestatemanager.presentation.model.asUiEstateType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class EstateDetailsFragment : Fragment() {
    //region Variables initialization
    private val viewModel: EstateDetailsViewModel by viewModels()
    private var _binding: FragmentEstateDetailsBinding? = null
    private val binding get() = _binding!!
    private var estateId: String? = null
    private lateinit var poiAdapter: PoiAdapter
    private lateinit var photoAdapter: PhotoAdapter
    private val API_KEY = BuildConfig.MAPS_API_KEY
    private var eventListener: EventListener? = null

    //endregion

    companion object {
        @JvmStatic
        fun newInstance(estateId: String) =
            EstateDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(Constants.ARG_ESTATE_ID, estateId)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        estateId = arguments?.getString(Constants.ARG_ESTATE_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEstateDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        eventListener = (requireActivity() as EventListener)
        estateId = arguments?.getString(Constants.ARG_ESTATE_ID)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpPhotosRecyclerView()
        setUpPoiRecyclerView()
        binding.editButton.setOnClickListener {
            eventListener?.onEvent(Event.OpenEditFragment(estateId))
        }
        binding.closeButton.setOnClickListener {
            findNavController().navigateUp()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    updateUi(uiState)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshEstateDetails()
    }

    //region UI updates
    private fun updateUi(uiState: EstateDetailsUiState) {
        when {
            uiState.isLoading -> {
                showLoadingView()
            }

            !uiState.loadingError.isNullOrEmpty() -> {
                showErrorView()
                binding.detailsErrorTextView.text = uiState.loadingError
            }

            uiState.estateWithDetails != null -> {
                showDetailsView()
                updateDetailsView(uiState.estateWithDetails, uiState.currencyCode)
            }
        }
    }

    //endregion
    private fun showLoadingView() {
        binding.detailsCircularProgressBar.visibility = View.VISIBLE
        binding.detailsErrorTextView.visibility = View.GONE
        binding.detailsRootLayout.visibility = View.GONE
    }

    private fun showErrorView() {
        binding.detailsCircularProgressBar.visibility = View.GONE
        binding.detailsErrorTextView.visibility = View.VISIBLE
        binding.detailsRootLayout.visibility = View.GONE
    }

    private fun showDetailsView() {
        binding.detailsCircularProgressBar.visibility = View.GONE
        binding.detailsErrorTextView.visibility = View.GONE
        binding.detailsRootLayout.visibility = View.VISIBLE
    }

    private fun updateDetailsView(estate: EstateWithDetails, currencyCode: CurrencyCode) {
        photoAdapter.submitList(estate.photos)
        poiAdapter.submitList(estate.nearbyPointsOfInterest)
        when (estate.estateStatus) {
            EstateStatus.AVAILABLE -> {
                with(binding.detailsStatusTextView) {
                    text = getString(R.string.available)
                    setTextColor(ContextCompat.getColor(context, R.color.green))
                }
                binding.detailsSaleDateLayout.visibility = View.GONE
            }

            EstateStatus.SOLD -> {
                with(binding.detailsStatusTextView) {
                    text = getString(R.string.sold)
                    setTextColor(ContextCompat.getColor(context, R.color.red))
                }
                binding.detailsSaleDateTextView.text = getFormattedDate(estate.saleDate)
            }
        }

        binding.detailsPriceTextView.text = priceToText(estate.price, currencyCode)
        binding.detailsCityTextView.text = estate.address.city
        binding.detailsEntryDateTextView.text = getFormattedDate(estate.entryDate)
        binding.detailsEstateTypeTextView.text = estate.type.asUiEstateType(requireContext()).name
        binding.detailsDescriptionTextView.text = estate.fullDescription

        binding.featuresTypeTextView.text = getString(
            R.string.estate_type,
            estate.type.asUiEstateType(requireContext()).name
        )
        binding.featuresRoomsCountTextView.text = getString(
            R.string.number_of_rooms,
            estate.roomsCount?.toString() ?: "--"
        )
        binding.featuresBedroomsCountTextView.text = getString(
            R.string.number_of_bedrooms,
            estate.bedroomsCount?.toString() ?: "--"
        )

        binding.featuresBathroomsCountTextView.text = getString(
            R.string.number_of_bathrooms,
            estate.bathroomsCount?.toString() ?: "--"
        )
        binding.featuresAreaTextView.text = getString(R.string.areaWithValue, estate.area)
        binding.addressTextView.text = estate.address.toRawString()
        binding.agentImageView.load(estate.agent.photoUrl) {
            transformations(CircleCropTransformation())
            placeholder(R.drawable.person)
            error(R.drawable.person_error)
        }
        "${estate.agent.firstName}  ${estate.agent.lastName[0]}.".also {
            binding.agentNameTextView.text = it
        }
        showEstateLocationOnMap(estate.address.location)
    }

    private fun updatePhotoCountText(currentPage: Int, totalPageCount: Int) {
        val countText = "${currentPage + 1} / $totalPageCount"
        binding.photoItemCountTextView.text = countText
    }

    // region RecyclerViews
    private fun setUpPhotosRecyclerView() {
        photoAdapter = PhotoAdapter()

        val viewPager = binding.viewPager
        viewPager.adapter = photoAdapter
        val totalCount = (viewPager.adapter as PhotoAdapter).itemCount

        updatePhotoCountText(viewPager.currentItem, totalCount)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val total = (viewPager.adapter as PhotoAdapter).itemCount
                updatePhotoCountText(position, total)
            }
        })

    }

    private fun setUpPoiRecyclerView() {
        val recyclerView = binding.detailsPoiRecyclerView
        poiAdapter = PoiAdapter()
        recyclerView.adapter = poiAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
    //endregion


    private fun showEstateLocationOnMap(location: Location) {
        val width = binding.root.width
        val height: Int = (width / 3.0).toInt()
        val staticMapUrl = STATIC_MAP_BASE_URL +
                "?center=${location.latitude},${location.longitude}" +
                "&$STATIC_MAP_ZOOM" +
                "&size=${width}x${height}" +
                "&$STATIC_MAP_SCALE" +
                "&$STATIC_MAP_MARKER_COLOR${location.latitude},${location.longitude}" +
                "&key=$API_KEY"

        binding.mapImageView.load(staticMapUrl) {
            placeholder(R.drawable.map_placeholder)
            error(R.drawable.ic_error)
        }
    }
}