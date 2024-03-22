package com.waminiyi.realestatemanager.features.loanSimulator

import com.waminiyi.realestatemanager.core.Constants.HUNDRED
import com.waminiyi.realestatemanager.core.Constants.MONTH_IN_YEAR
import kotlin.math.pow

data class Loan(
    val amount: Int,
    val tenureInYears: Int,
    val initialPayment: Int,
    val monthlyIncome: Int,
    val otherOngoingLoanMonthlyPayment: Int,
    val isForNewEstate: Boolean = true,
    val estimatedWarrantyCostsRate: Double = 0.10,
    val estimatedNotaryFeesRate: Double = 3.5,
    val estimatedLoanInterestRate: Double = 4.0,
) {

    val estimatedTotalNotaryFees: Int
        get() {
            return (amount * estimatedNotaryFeesRate / HUNDRED).toInt()
        }

    val estimatedTotalWarrantyCosts: Int
        get() {
            return (amount * estimatedWarrantyCostsRate / HUNDRED).toInt()
        }

    val estimatedMonthlyWarrantyCosts: Double
        get() {
            return estimatedTotalWarrantyCosts.toDouble() / (MONTH_IN_YEAR * tenureInYears)
        }


    val estimatedTotalLoanInterest: Double
        get() {
            return MONTH_IN_YEAR * tenureInYears * estimatedMonthlyLoanPayment - estimatedTotalLoanAmount
        }

    val estimatedMonthlyLoanPayment: Double
        get() {
            return (estimatedTotalLoanAmount * estimatedLoanInterestRate / MONTH_IN_YEAR) /
                    (1 - (1 + estimatedLoanInterestRate / MONTH_IN_YEAR).pow(-MONTH_IN_YEAR * tenureInYears))
        }

    private val estimatedTotalLoanAmount: Int
        get() {
            return amount - initialPayment + estimatedTotalNotaryFees
        }

    val debtRate: Double
        get() {
            return ((estimatedMonthlyLoanPayment + otherOngoingLoanMonthlyPayment) /
                    monthlyIncome.toDouble()) * HUNDRED
        }
}
