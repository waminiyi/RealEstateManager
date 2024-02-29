package com.waminiyi.realestatemanager.features.agentList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.core.Constants
import com.waminiyi.realestatemanager.databinding.FragmentAgentListBinding
import com.waminiyi.realestatemanager.features.agentList.adapters.AgentAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AgentListFragment : Fragment() {
    private val viewModel: AgentListViewModel by viewModels()
    private var _binding: FragmentAgentListBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = AgentListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgentListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val recyclerView = binding.recyclerview
        val adapter = AgentAdapter(onAgentClicked = {
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
                            binding.agentsListCircularProgressBar.visibility = View.VISIBLE
                            binding.agentListErrorTextView.visibility = View.GONE
                        }

                        uiState.isError -> {
                            binding.recyclerview.visibility = View.GONE
                            binding.agentsListCircularProgressBar.visibility = View.GONE
                            binding.agentListErrorTextView.visibility = View.VISIBLE
                            binding.agentListErrorTextView.text = uiState.errorMessage
                        }

                        uiState.agents.isNotEmpty() -> {
                            binding.recyclerview.visibility = View.VISIBLE
                            binding.agentsListCircularProgressBar.visibility = View.GONE
                            binding.agentListErrorTextView.visibility = View.GONE
                            adapter.submitList(uiState.agents)
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
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.agents_list_appbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.navigation_add_agent -> {
                        navigateToAddAgentScreen()
                        return true
                    }
                }
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshAgents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToDetailsFragment(agentUuid: String) {
        val bundle = Bundle().apply { putString(Constants.ARG_AGENT_ID, agentUuid) }
        findNavController().navigate(R.id.agentDetailsFragment, bundle)
    }

    private fun navigateToAddAgentScreen() {
        findNavController().navigate(R.id.navigation_add_agent)
    }

}