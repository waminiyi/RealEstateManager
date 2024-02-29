package com.waminiyi.realestatemanager.features.estateslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.databinding.FragmentListAndMapContainerBinding

class ListAndMapContainerFragment : Fragment() {

    companion object {
        fun newInstance() = ListAndMapContainerFragment()
    }

    private var _binding: FragmentListAndMapContainerBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ListAndMapContainerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListAndMapContainerBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.listViewButton.text = "List"
        binding.mapViewButton.text = "Map"
        showListViewFragment()
        binding.listViewButton.setOnClickListener {
            showListViewFragment()
        }

        binding.mapViewButton.setOnClickListener {
            showMapViewFragment()
        }
        return root
    }

    private fun showMapViewFragment() {
        val mapViewFragment = EstateMapFragment()
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, mapViewFragment)
        fragmentTransaction.commit()
    }

    private fun showListViewFragment() {
        val listViewFragment = EstateListFragment()
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, listViewFragment)
        fragmentTransaction.commit()
    }

}