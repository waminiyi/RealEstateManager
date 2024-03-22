package com.waminiyi.realestatemanager.features.loanSimulator

data class LoanSimulatorUiState(
    val loan: Loan = Loan(
        amount = 200000,
        tenureInYears = 20,
        initialPayment = 20000,
        monthlyIncome = 3000,
        otherOngoingLoanMonthlyPayment = 0,
        isForNewEstate = true,
        estimatedWarrantyCostsRate = 0.10,
        estimatedNotaryFeesRate = 3.5,
        estimatedLoanInterestRate = 4.0
    )
)
