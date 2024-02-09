package com.waminiyi.realestatemanager.features.agentDetails

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.waminiyi.realestatemanager.R

class AgentDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = AgentDetailsFragment()
    }

    private lateinit var viewModel: AgentDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_agent_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AgentDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}