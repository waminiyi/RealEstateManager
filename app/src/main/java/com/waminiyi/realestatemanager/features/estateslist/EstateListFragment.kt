package com.waminiyi.realestatemanager.features.estateslist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.databinding.FragmentEstateListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EstateListFragment : Fragment() {
    private val viewModel: EstateListViewModel by viewModels()

    private var _binding: FragmentEstateListBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
/*        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.removeItem(R.id.navigation_edit)
                menu.removeItem(R.id.save_estate_menu_item)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToDetailsFragment(estateUuid: String) {
        Toast.makeText(context, "estate with id $estateUuid clicked", Toast.LENGTH_LONG).show()
    }
}