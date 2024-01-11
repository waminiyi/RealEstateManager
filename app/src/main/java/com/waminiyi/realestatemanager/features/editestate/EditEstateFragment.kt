package com.waminiyi.realestatemanager.features.editestate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.waminiyi.realestatemanager.databinding.FragmentEditestateBinding

class EditEstateFragment : Fragment() {

    private var _binding: FragmentEditestateBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val editestateViewModel =
            ViewModelProvider(this).get(EditestateViewModel::class.java)

        _binding = FragmentEditestateBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*ateViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}