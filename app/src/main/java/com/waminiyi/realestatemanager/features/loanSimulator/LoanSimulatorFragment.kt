package com.waminiyi.realestatemanager.features.loanSimulator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.features.events.Event
import com.waminiyi.realestatemanager.features.events.EventListener

class LoanSimulatorFragment : Fragment() {

    companion object {
        fun newInstance() = LoanSimulatorFragment()
    }

    private lateinit var viewModel: LoanSimulatorViewModel
    private var eventListener: EventListener? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_loan_simulator, container, false)
        eventListener = (requireActivity() as EventListener)

        view.findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            Toast.makeText(requireContext(), "Try to close", Toast.LENGTH_LONG).show()
            eventListener?.onEvent(Event.HideRightFragment)
        }


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoanSimulatorViewModel::class.java)
        // TODO: Use the ViewModel
    }

}