package com.waminiyi.realestatemanager.features.estatesListView

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.core.Constants
import com.waminiyi.realestatemanager.core.Constants.TAB_LANDSCAPE_LAYOUT_MODE
import com.waminiyi.realestatemanager.core.Constants.TAB_LAYOUT_MODE
import com.waminiyi.realestatemanager.databinding.FragmentEstateListBinding
import com.waminiyi.realestatemanager.features.estateListing.EstateListingViewModel
import com.waminiyi.realestatemanager.features.events.Event
import com.waminiyi.realestatemanager.features.events.EventListener
import com.waminiyi.realestatemanager.features.events.ScreenSplitListener
import com.waminiyi.realestatemanager.features.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EstateListFragment : Fragment(), ScreenSplitListener {
    private val viewModel: EstateListingViewModel by activityViewModels()
    private var _binding: FragmentEstateListBinding? = null
    private val binding get() = _binding!!
    private var eventListener: EventListener? = null
    private var columnCount = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEstateListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = binding.recyclerview
        val adapter = EstateListAdapter(onEstateSelected = {
            eventListener?.onEvent(Event.EstateClicked(it))
        })
        val mode = resources.getInteger(R.integer.layout_mode)
        columnCount = if (mode == TAB_LAYOUT_MODE || mode ==TAB_LANDSCAPE_LAYOUT_MODE) {
            2
        } else {
            1
        }
        eventListener = (requireActivity() as EventListener)
        (requireActivity() as HomeActivity).setScreenSplitListener(this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, columnCount)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                when {
                    uiState.isLoading -> {
                        binding.recyclerview.visibility = View.GONE
                        binding.estateListCircularProgressBar.visibility = View.VISIBLE
                        binding.estateListErrorTextView.visibility = View.GONE
                    }

                    uiState.isError -> {
                        binding.recyclerview.visibility = View.GONE
                        binding.estateListCircularProgressBar.visibility = View.GONE
                        binding.estateListErrorTextView.visibility = View.VISIBLE
                        binding.estateListErrorTextView.text = uiState.errorMessage
                    }

                    else -> {
                        binding.recyclerview.visibility = View.VISIBLE
                        binding.estateListCircularProgressBar.visibility = View.GONE
                        binding.estateListErrorTextView.visibility = View.GONE
                        adapter.submitList(uiState.estates)
                        adapter.updateCurrencyCode(uiState.currencyCode)
                        Log.d("UI STATE", uiState.toString())
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onScreenSplittingChanged(isSplit: Boolean) {
        columnCount = if (isSplit) {
            1
        } else {
            2
        }
        binding.recyclerview.layoutManager = GridLayoutManager(context, columnCount)
    }
}