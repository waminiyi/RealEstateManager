package com.waminiyi.realestatemanager.features.estatesListView

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.core.Constants
import com.waminiyi.realestatemanager.databinding.FragmentEstateListBinding
import com.waminiyi.realestatemanager.features.estateListing.EstateListingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EstateListFragment : Fragment() {
    private val viewModel: EstateListingViewModel by viewModels()
    private var _binding: FragmentEstateListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEstateListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val recyclerView = binding.recyclerview
        val adapter = EstateListAdapter(onEstateSelected = {
            navigateToDetailsFragment(it)
        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        val fragmentScope = CoroutineScope(Dispatchers.Main)

        fragmentScope.launch {
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

                        uiState.estates.isNotEmpty() -> {
                            binding.recyclerview.visibility = View.VISIBLE
                            binding.estateListCircularProgressBar.visibility = View.GONE
                            binding.estateListErrorTextView.visibility = View.GONE
                            adapter.submitList(uiState.estates)
                            adapter.updateCurrencyCode(uiState.currencyCode)
                        }

                        else -> {
                            Log.d("UISTATE", uiState.toString())
                        }
                    }
                }
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToDetailsFragment(estateUuid: String) {
        val bundle = Bundle().apply { putString(Constants.ARG_ESTATE_ID, estateUuid) }
        findNavController().navigate(R.id.navigation_estatedetails, bundle)
    }
}