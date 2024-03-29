package com.waminiyi.realestatemanager.features.estatesListView

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.databinding.FragmentEstateListBinding
import com.waminiyi.realestatemanager.events.Event
import com.waminiyi.realestatemanager.events.EventListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EstateListFragment : Fragment() {
    private val viewModel: EstateListViewModel by viewModels()
    private var _binding: FragmentEstateListBinding? = null
    private val binding get() = _binding!!
    private var eventListener: EventListener? = null

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
        eventListener = (requireActivity() as EventListener)

        recyclerView.adapter = adapter

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

                    uiState.estates.isEmpty() -> {
                        binding.recyclerview.visibility = View.GONE
                        binding.estateListCircularProgressBar.visibility = View.GONE
                        binding.estateListErrorTextView.visibility = View.VISIBLE
                        binding.estateListErrorTextView.text = getString(R.string.no_estate_found)
                    }

                    else -> {
                        binding.recyclerview.visibility = View.VISIBLE
                        binding.estateListCircularProgressBar.visibility = View.GONE
                        binding.estateListErrorTextView.visibility = View.GONE
                        binding.recyclerview.layoutManager = GridLayoutManager(context, uiState.estateListColumnCount)
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

}