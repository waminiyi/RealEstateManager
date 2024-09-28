package com.waminiyi.realestatemanager.presentation.estateMapView

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.Constants.MAP_VIEW_DEFAULT_ZOOM_LEVEL
import com.waminiyi.realestatemanager.data.models.Estate
import com.waminiyi.realestatemanager.data.models.EstateStatus
import com.waminiyi.realestatemanager.util.network.NetworkMonitor
import com.waminiyi.realestatemanager.databinding.FragmentEstateMapBinding
import com.waminiyi.realestatemanager.events.Event
import com.waminiyi.realestatemanager.events.EventListener
import com.waminiyi.realestatemanager.presentation.model.asUiEstateType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class EstateMapFragment : Fragment(), OnMapReadyCallback {

    private val viewModel: EstateMapViewModel by viewModels()
    private var _binding: FragmentEstateMapBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var networkMonitor: NetworkMonitor
    private var eventListener: EventListener? = null
    private var map: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEstateMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMapIfNeeded()
        eventListener = (requireActivity() as EventListener)

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

    private fun updateUi(uiState: EstateMapUiState) {
        when {
            !uiState.isOnline -> {
                showErrorView(getString(R.string.map_loading_default_error))
            }

            uiState.isLoading -> {
                showLoadingView()
            }

            uiState.isError -> {
                showErrorView(uiState.errorMessage)
            }

            uiState.estates.isNotEmpty() -> {
                showMapView()
                markEstatesPositionsOnMap(uiState.estates)
            }
        }
    }

    private fun showLoadingView() {
        binding.progressBar.visibility = View.VISIBLE
        binding.mapErrorTextView.visibility = View.GONE
        binding.mapFragmentContainer.visibility = View.GONE
    }

    private fun showErrorView(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.mapErrorTextView.visibility = View.VISIBLE
        binding.mapFragmentContainer.visibility = View.GONE
        binding.mapErrorTextView.text = message
    }

    private fun showMapView() {
        binding.progressBar.visibility = View.GONE
        binding.mapErrorTextView.visibility = View.GONE
        binding.mapFragmentContainer.visibility = View.VISIBLE
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        map?.setOnMarkerClickListener {
            if (it.tag != null) {
                val id: String = it.tag as String
                eventListener?.onEvent(Event.EstateClicked(id))
            }
            return@setOnMarkerClickListener true

        }
        Log.e("Map ready", map.toString())
        try {
            val success = map!!.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(), R.raw.map_style
                )
            )
            if (!success) {
                Log.e("Map style", "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("Map style", "Can't find style. Error: ", e)
        }
    }

    private fun setupMapIfNeeded() {
        if (map == null) {
            val mapFragment = childFragmentManager
                .findFragmentById(R.id.mapFragmentContainer) as SupportMapFragment?
            mapFragment?.getMapAsync(this)
        }
    }

    private fun markEstatesPositionsOnMap(estates: List<Estate>) {
        map?.let {
            it.clear()
            estates.forEach { estate ->
                val latLng = LatLng(estate.location.latitude, estate.location.longitude)
                val options = MarkerOptions()
                options.position(latLng)
                options.title(estate.type.asUiEstateType(requireContext()).name)
                options.icon(
                    createMarkerBitmapDescriptor(
                        when (estate.status) {
                            EstateStatus.AVAILABLE -> ContextCompat.getColor(requireContext(), R.color.green)
                            EstateStatus.SOLD -> ContextCompat.getColor(requireContext(), R.color.red)
                        }
                    )
                )
                val marker = it.addMarker(options)
                marker.tag = estate.uuid
                it.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, MAP_VIEW_DEFAULT_ZOOM_LEVEL))
            }
        }
    }


    private fun createMarkerBitmapDescriptor(@ColorInt colorID: Int): BitmapDescriptor? {

        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_home_marker)?.apply {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            mutate()
        } ?: return null
        DrawableCompat.setTint(drawable, colorID)

        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        drawable.draw(Canvas(bitmap))

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}