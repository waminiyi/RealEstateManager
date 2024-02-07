package com.waminiyi.realestatemanager.features.estatedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.core.Constants
import com.waminiyi.realestatemanager.core.model.data.EstateStatus
import com.waminiyi.realestatemanager.core.model.data.EstateWithDetails
import com.waminiyi.realestatemanager.core.model.data.toRawString
import com.waminiyi.realestatemanager.core.util.util.formatAsUSDollar
import com.waminiyi.realestatemanager.core.util.util.getFormattedDate
import com.waminiyi.realestatemanager.databinding.FragmentEstateDetailsBinding
import com.waminiyi.realestatemanager.features.estatedetails.adapters.PhotoAdapter
import com.waminiyi.realestatemanager.features.estatedetails.adapters.PoiAdapter
import com.waminiyi.realestatemanager.features.model.asUiEstateType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
        estateId = arguments?.getString(Constants.ARG_ESTATE_ID)
        val fragmentScope = CoroutineScope(Dispatchers.Main)

        fragmentScope.launch {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.uiState.collect { uiState ->
                    updateUi(uiState)
                }
            }
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.details_fragment_appbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.navigation_edit -> {
                        openEditEstateFragment(estateId)
                        return true
                    }
                }
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        setUpPhotosRecyclerView()
        setUpPoiRecyclerView()
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
                updateDetailsView(uiState.estateWithDetails)
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

    private fun updateDetailsView(estate: EstateWithDetails) {
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

        binding.detailsPriceTextView.text = estate.price.formatAsUSDollar()
        binding.detailsCityTextView.text = estate.address.city
        binding.detailsEntryDateTextView.text = getFormattedDate(estate.entryDate)
        binding.detailsEstateTypeTextView.text = estate.type.asUiEstateType(requireContext()).name
        binding.detailsRoomsCountTextView.text =
            resources.getQuantityString(R.plurals.roomsCount, estate.roomsCount, estate.roomsCount)
        binding.detailsAreaTextView.text = getString(R.string.areaInSquareMeter, estate.area.toInt())
        binding.detailsDescriptionTextView.text = estate.fullDescription
        binding.featuresTypeTextView.text = estate.type.asUiEstateType(requireContext()).name
        binding.featuresRoomsCountTextView.text = estate.roomsCount.toString()
        binding.featuresBedroomsCountTextView.text = estate.bedroomsCount.toString()
        binding.featuresBathroomsCountTextView.text = estate.bathroomsCount.toString()
        binding.featuresAreaTextView.text = getString(R.string.areaInSquareMeter, estate.area.toInt())
        binding.addressTextView.text = estate.address.toRawString()
        binding.agentImageView.load(estate.agent.photoUrl) {
            transformations(CircleCropTransformation())
            placeholder(R.drawable.person)
            error(R.drawable.person_error)
        }
        "${estate.agent.firstName}  ${estate.agent.lastName[0]}.".also {
            binding.agentNameTextView.text = it
        }
    }

    private fun openEditEstateFragment(estateUuid: String?) {
        estateUuid?.let {
            val bundle = Bundle().apply { putString(Constants.ARG_ESTATE_ID, it) }
            findNavController().navigate(R.id.navigation_add, bundle)
        }
    }

    private fun updatePhotoCountView(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
        layoutManager?.let {
            val indexText = "${it.findFirstVisibleItemPosition() + 1} / ${it.itemCount}"
            binding.photoItemCountTextView.text = indexText
        }
    }

    // region RecyclerViews
    private fun setUpPhotosRecyclerView() {
        val recyclerView = binding.detailsPhotosRecyclerView
        photoAdapter = PhotoAdapter()
        recyclerView.adapter = photoAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        updatePhotoCountView(recyclerView)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                updatePhotoCountView(recyclerView)
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
}