package com.waminiyi.realestatemanager.presentation.loanSimulator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.util.util.roundToTwoDigits
import com.waminiyi.realestatemanager.databinding.FragmentLoanSimulatorBinding
import com.waminiyi.realestatemanager.events.Event
import com.waminiyi.realestatemanager.events.EventListener
import com.waminiyi.realestatemanager.presentation.extensions.afterTextChanged
import com.waminiyi.realestatemanager.presentation.extensions.showInformationDialog
import com.waminiyi.realestatemanager.presentation.extensions.updateValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoanSimulatorFragment : Fragment() {

    companion object {
        fun newInstance() = LoanSimulatorFragment()
    }

    private var eventListener: EventListener? = null
    private var _binding: FragmentLoanSimulatorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoanSimulatorViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoanSimulatorBinding.inflate(inflater, container, false)
        eventListener = (requireActivity() as EventListener)
        setUpTextChangedListeners()
        val fragmentScope = CoroutineScope(Dispatchers.Main)

        fragmentScope.launch {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.uiState.collect { uiState ->
                    if (uiState.isInitialState) {
                        setInitialValues(uiState)
                    }
                    updateUi(uiState)
                }
            }
        }

        binding.closeButton.setOnClickListener {
            eventListener?.onEvent(Event.HideRightFragment)
        }

        binding.estatePriceLabelTextView.setOnClickListener {
            requireContext().showInformationDialog(getString(R.string.loan_price_information_text))
        }

        return binding.root
    }

    private fun setUpTextChangedListeners() {
        binding.priceTextInputEditText.afterTextChanged {
            viewModel.updateAmount(it.toFloatOrNull() ?: 0F)
        }
        binding.tenureTextInputEditText.afterTextChanged {
            viewModel.updateTenureInYears(it.toIntOrNull() ?: 0)
        }
        binding.initialPaymentTextInputEditText.afterTextChanged {
            viewModel.updateInitialPayment(it.toFloatOrNull() ?: 0F)
        }
        binding.interestRateTextInputEditText.afterTextChanged {
            viewModel.updateEstimatedLoanInterestRate(it.toFloatOrNull() ?: 0F)
        }

        binding.notaryFeesRateTextInputEditText.afterTextChanged {
            viewModel.updateEstimatedNotaryFeesRate(it.toFloatOrNull() ?: 0F)
        }

        binding.warrantyCostsRateTextInputEditText.afterTextChanged {
            viewModel.updateEstimatedWarrantyCostsRate(it.toFloatOrNull() ?: 0F)
        }

        binding.monthlyIncomeTextInputEditText.afterTextChanged {
            viewModel.updateMonthlyIncome(it.toFloatOrNull() ?: 0F)
        }

        binding.otherLoanPaymentTextInputEditText.afterTextChanged {
            viewModel.updateOtherOngoingLoanMonthlyPayment(it.toFloatOrNull() ?: 0F)
        }
    }

    private fun updateUi(uiState: LoanSimulatorUiState) {
        binding.debtRateTextView.text = getString(
            R.string.rate,
            uiState.loan.debtRate.roundToTwoDigits().toString()
        )
        binding.monthlyPaymentTextView.text = getString(
            R.string.amount,
            uiState.loan.monthlyLoanPayment.roundToTwoDigits().toString()
        )
        binding.monthlyWarrantyTextView.text = getString(
            R.string.amount,
            uiState.loan.monthlyWarrantyCosts.roundToTwoDigits().toString()
        )
        binding.notaryFeesTextView.text = getString(
            R.string.amount,
            uiState.loan.totalNotaryFees.roundToTwoDigits().toString()
        )
        binding.warrantyCostsTextView.text = getString(
            R.string.amount,
            uiState.loan.totalWarrantyCosts.roundToTwoDigits().toString()
        )
        binding.totalLoanInterestTextView.text = getString(
            R.string.amount,
            uiState.loan.totalLoanInterest.roundToTwoDigits().toString()
        )
    }

    private fun setInitialValues(uiState: LoanSimulatorUiState) {
        binding.priceTextInputEditText.updateValue(uiState.loan.amount.toString())
        binding.tenureTextInputEditText.updateValue(uiState.loan.tenureInYears.toString())
        binding.initialPaymentTextInputEditText.updateValue(uiState.loan.initialPayment.toString())
        binding.interestRateTextInputEditText.updateValue(uiState.loan.loanInterestRate.toString())
        binding.notaryFeesRateTextInputEditText.updateValue(uiState.loan.notaryFeesRate.toString())
        binding.warrantyCostsRateTextInputEditText.updateValue(uiState.loan.warrantyCostsRate.toString())
        binding.monthlyIncomeTextInputEditText.updateValue(uiState.loan.monthlyIncome.toString())
        binding.otherLoanPaymentTextInputEditText.updateValue(uiState.loan.otherOngoingLoanMonthlyPayment.toString())
        viewModel.updateInitialStateLoading(true)
    }
}