package com.waminiyi.realestatemanager.features.addOrEditAgent

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.waminiyi.realestatemanager.R

class AddOrEditAgentFragment : Fragment() {

    companion object {
        fun newInstance() = AddOrEditAgentFragment()
    }

    private lateinit var viewModel: AddOrEditAgentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_or_edit_agent, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddOrEditAgentViewModel::class.java)
        // TODO: Use the ViewModel
    }

}