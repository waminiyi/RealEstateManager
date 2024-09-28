package com.waminiyi.realestatemanager.presentation.estateMapView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.data.models.Estate
import com.waminiyi.realestatemanager.util.util.CurrencyCode
import com.waminiyi.realestatemanager.presentation.estatesListView.EstateItemView

class EstateBottomSheetDialog : BottomSheetDialogFragment() {

    override fun getTheme(): Int {
        return R.style.EstateBottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val estateJson: String? = arguments?.getString(ARG_ESTATE)
        val currencyCodeJson = arguments?.getString(ARG_CURRENCY_CODE)

        val estate: Estate? = Gson().fromJson(estateJson, Estate::class.java)
        val currencyCode: CurrencyCode? = Gson().fromJson(currencyCodeJson, CurrencyCode::class.java)

        val itemView = EstateItemView(requireContext())
        estate?.let {
            itemView.bind(estate, currencyCode ?: CurrencyCode.USD)
        }
        return EstateItemView(requireContext())
    }


    companion object {
        private const val ARG_ESTATE = "arg_estate"
        private const val ARG_CURRENCY_CODE = "arg_currency_code"

        // Function to create a new instance of the fragment with arguments
        fun newInstance(estate: Estate, currencyCode: CurrencyCode): EstateBottomSheetDialog {
            val fragment = EstateBottomSheetDialog()
            val args = Bundle().apply {
                putString(ARG_ESTATE, Gson().toJson(estate))
                putSerializable(ARG_CURRENCY_CODE, Gson().toJson(currencyCode))
            }
            fragment.arguments = args
            return fragment
        }
    }
}