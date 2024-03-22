package com.waminiyi.realestatemanager.features.loanSimulator

import com.waminiyi.realestatemanager.core.Constants.HUNDRED
import com.waminiyi.realestatemanager.core.Constants.MONTH_IN_YEAR
import kotlin.math.pow

data class Loan(
    val amount: Float,
    val tenureInYears: Int,
    val initialPayment: Float,
    val monthlyIncome: Float,
    val otherOngoingLoanMonthlyPayment: Float,
    val isForNewEstate: Boolean = true,
    val warrantyCostsRate: Float = 0.10F,
    val notaryFeesRate: Float = 3.5F,
    val loanInterestRate: Float = 4.0F,
) {

    val totalNotaryFees: Float = (amount * notaryFeesRate / HUNDRED.toFloat())
    private val totalLoanAmount: Float = amount - initialPayment + totalNotaryFees

    val monthlyLoanPayment: Float = (totalLoanAmount * loanInterestRate * 0.01F / MONTH_IN_YEAR.toFloat()) /
            (1.toFloat() - (1.toFloat() + loanInterestRate * 0.01F / MONTH_IN_YEAR.toFloat()).pow(-MONTH_IN_YEAR * tenureInYears))

    val totalWarrantyCosts: Float = (totalLoanAmount * warrantyCostsRate * 0.01F * tenureInYears)

    val monthlyWarrantyCosts: Float =
        totalWarrantyCosts / (MONTH_IN_YEAR.toFloat() * tenureInYears.toFloat())


    val totalLoanInterest: Float = MONTH_IN_YEAR.toFloat() * tenureInYears.toFloat() * monthlyLoanPayment - totalLoanAmount


    val debtRate: Float = ((monthlyLoanPayment + otherOngoingLoanMonthlyPayment) /
            monthlyIncome) * HUNDRED

}
