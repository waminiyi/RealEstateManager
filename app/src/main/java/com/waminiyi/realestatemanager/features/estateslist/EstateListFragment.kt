package com.waminiyi.realestatemanager.features.estateslist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.databinding.FragmentEstateListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EstateListFragment : Fragment() {
    private val viewModel: EstateListViewModel by viewModels()

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
        val adapter = EstateListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        val fragmentScope = CoroutineScope(Dispatchers.Main)

        fragmentScope.launch {
            viewModel.uiState.collect { uiState ->
                when {
                    uiState.isLoading -> {
                        Log.d("UISTATE","LOADING")
                    }

                    uiState.isError -> {
                        Log.d("UISTATE","ERROR")
                        binding.errorTv.text=uiState.errorMessage
                    }

                    uiState.estates.isNotEmpty() -> {
                        Log.d("UISTATE","NOTEMPTY")
                        adapter.submitList(uiState.estates)
                    }

                    else -> {
                        Log.d("UISTATE","EMPTY")
                    }
                }
            }
        }

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}