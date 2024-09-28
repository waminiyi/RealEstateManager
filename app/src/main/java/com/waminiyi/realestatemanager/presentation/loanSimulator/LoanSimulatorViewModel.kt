package com.waminiyi.realestatemanager.presentation.loanSimulator

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.waminiyi.realestatemanager.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoanSimulatorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var amount: Float? = savedStateHandle[Constants.ARG_AMOUNT]

    private val _uiState = MutableStateFlow(LoanSimulatorUiState())
    val uiState: StateFlow<LoanSimulatorUiState> = _uiState.asStateFlow()

    init {
        amount?.let {
            updateAmount(it)
        }
    }

    fun updateAmount(amount: Float) {
        _uiState.update { it.copy(loan = it.loan.copy(amount = amount)) }
    }

    fun updateTenureInYears(tenureInYears: Int) {
        _uiState.update { it.copy(loan = it.loan.copy(tenureInYears = tenureInYears)) }
    }

    fun updateInitialPayment(initialPayment: Float) {
        _uiState.update { it.copy(loan = it.loan.copy(initialPayment = initialPayment)) }
    }

    fun updateMonthlyIncome(monthlyIncome: Float) {
        _uiState.update { it.copy(loan = it.loan.copy(monthlyIncome = monthlyIncome)) }
    }

    fun updateOtherOngoingLoanMonthlyPayment(otherOngoingLoanMonthlyPayment: Float) {
        _uiState.update {
            it.copy(loan = it.loan.copy(otherOngoingLoanMonthlyPayment = otherOngoingLoanMonthlyPayment))
        }
    }

    fun updateEstimatedWarrantyCostsRate(estimatedWarrantyCostsRate: Float) {
        _uiState.update { it.copy(loan = it.loan.copy(warrantyCostsRate = estimatedWarrantyCostsRate)) }
    }

    fun updateEstimatedNotaryFeesRate(estimatedNotaryFeesRate: Float) {
        _uiState.update { it.copy(loan = it.loan.copy(notaryFeesRate = estimatedNotaryFeesRate)) }
    }

    fun updateEstimatedLoanInterestRate(estimatedLoanInterestRate: Float) {
        _uiState.update { it.copy(loan = it.loan.copy(loanInterestRate = estimatedLoanInterestRate)) }
    }

    fun updateInitialStateLoading(wasLoaded: Boolean) {
        _uiState.update { it.copy(isInitialState = !wasLoaded) }
    }
}