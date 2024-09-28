package com.waminiyi.realestatemanager.presentation.loanSimulator

import com.waminiyi.realestatemanager.Constants.HUNDRED
import com.waminiyi.realestatemanager.Constants.MONTH_IN_YEAR
import kotlin.math.pow

/**
 * Data class representing a loan.
 * @property amount The total loan amount.
 * @property tenureInYears The tenure of the loan in years.
 * @property initialPayment The initial payment made for the loan.
 * @property monthlyIncome The monthly income of the borrower.
 * @property otherOngoingLoanMonthlyPayment The monthly payment for other ongoing loans.
 * @property warrantyCostsRate The rate of warranty costs (default is 10%).
 * @property notaryFeesRate The rate of notary fees (default is 3.5%).
 * @property loanInterestRate The interest rate of the loan (default is 4%).
 */
data class Loan(
    val amount: Float,
    val tenureInYears: Int,
    val initialPayment: Float,
    val monthlyIncome: Float,
    val otherOngoingLoanMonthlyPayment: Float,
    val warrantyCostsRate: Float = 0.10F,
    val notaryFeesRate: Float = 3.5F,
    val loanInterestRate: Float = 4.0F,
) {
    /**
     * Total notary fees for the loan.
     */
    val totalNotaryFees: Float = (amount * notaryFeesRate / HUNDRED.toFloat())
    private val totalLoanAmount: Float = amount - initialPayment + totalNotaryFees

    /**
     * Monthly payment for the loan.
     */
    val monthlyLoanPayment: Float = (totalLoanAmount * loanInterestRate / (MONTH_IN_YEAR * HUNDRED)) /
            (1 - (1 + loanInterestRate / (MONTH_IN_YEAR * HUNDRED)).pow(-MONTH_IN_YEAR * tenureInYears))

    /**
     * Total warranty costs for the loan.
     */
    val totalWarrantyCosts: Float = (totalLoanAmount * tenureInYears * warrantyCostsRate / HUNDRED)

    /**
     * Monthly warranty costs for the loan.
     */
    val monthlyWarrantyCosts: Float =
        totalWarrantyCosts / (MONTH_IN_YEAR.toFloat() * tenureInYears.toFloat())

    /**
     * Total loan interest paid over the tenure.
     */
    val totalLoanInterest: Float =
        MONTH_IN_YEAR.toFloat() * tenureInYears.toFloat() * monthlyLoanPayment - totalLoanAmount

    /**
     * Debt rate as a percentage of the monthly income.
     */
    val debtRate: Float = ((monthlyLoanPayment + otherOngoingLoanMonthlyPayment) /
            monthlyIncome) * HUNDRED

}
