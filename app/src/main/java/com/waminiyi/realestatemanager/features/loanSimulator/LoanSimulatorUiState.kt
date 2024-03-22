package com.waminiyi.realestatemanager.features.loanSimulator

data class LoanSimulatorUiState(
    val loan: Loan = Loan(
        amount = 200000F,
        tenureInYears = 20,
        initialPayment = 20000F,
        monthlyIncome = 3000F,
        otherOngoingLoanMonthlyPayment = 0F,
        warrantyCostsRate = 0.10F,
        notaryFeesRate = 3.5F,
        loanInterestRate = 4.0F
    ),
    val isInitialState: Boolean = true
)
