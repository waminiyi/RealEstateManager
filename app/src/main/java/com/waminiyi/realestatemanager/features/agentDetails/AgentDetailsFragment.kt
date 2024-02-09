package com.waminiyi.realestatemanager.features.agentDetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.core.Constants
import com.waminiyi.realestatemanager.core.model.data.Agent
import com.waminiyi.realestatemanager.databinding.FragmentAgentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AgentDetailsFragment : Fragment() {

    //region Variables initialization
    private val viewModel: AgentDetailsViewModel by viewModels()
    private var _binding: FragmentAgentDetailsBinding? = null
    private val binding get() = _binding!!
    private var agentId: String? = null
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        agentId = arguments?.getString(Constants.ARG_AGENT_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgentDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
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
                        openEditAgentFragment(agentId)
                        Log.d("menu", "clicked")
                        return true
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshAgentDetails()
    }

    //region UI updates
    private fun updateUi(uiState: AgentDetailsUiState) {
        when {
            uiState.isLoading -> {
                showLoadingView()
            }

            !uiState.loadingError.isNullOrEmpty() -> {
                showErrorView()
                binding.agentDetailsErrorTextView.text = uiState.loadingError
            }

            uiState.agent != null -> {
                showDetailsView()
                updateDetailsView(uiState.agent)
            }
        }
    }

    //endregion
    private fun showLoadingView() {
        binding.agentDetailsCircularProgressBar.visibility = View.VISIBLE
        binding.agentDetailsErrorTextView.visibility = View.GONE
        binding.detailsRootLayout.visibility = View.GONE
    }

    private fun showErrorView() {
        binding.agentDetailsCircularProgressBar.visibility = View.GONE
        binding.agentDetailsErrorTextView.visibility = View.VISIBLE
        binding.detailsRootLayout.visibility = View.GONE
    }

    private fun showDetailsView() {
        binding.agentDetailsCircularProgressBar.visibility = View.GONE
        binding.agentDetailsErrorTextView.visibility = View.GONE
        binding.detailsRootLayout.visibility = View.VISIBLE
    }

    private fun updateDetailsView(agent: Agent) {

        binding.firstNameTextView.text = agent.firstName
        binding.lastNameTextView.text = agent.lastName
        binding.emailTextView.text = agent.email
        binding.phoneTextView.text = agent.phoneNumber
        binding.agentPhotoImageView.load(agent.photoUrl) {
            transformations(CircleCropTransformation())
            placeholder(R.drawable.person)
            error(R.drawable.person_error)
        }
    }

    private fun openEditAgentFragment(agentUuid: String?) {
        agentUuid?.let {
            val bundle = Bundle().apply { putString(Constants.ARG_AGENT_ID, it) }
            findNavController().navigate(R.id.navigation_add_agent, bundle)
        }
    }
}