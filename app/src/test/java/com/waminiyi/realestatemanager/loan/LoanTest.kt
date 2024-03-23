package com.waminiyi.realestatemanager.loan

import com.waminiyi.realestatemanager.features.loanSimulator.Loan
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LoanTest {

    private lateinit var loan: Loan

    @Before
    fun setUp() {
        loan = Loan(
            amount = 100000f,
            tenureInYears = 20,
            initialPayment = 10000f,
            monthlyIncome = 3000f,
            otherOngoingLoanMonthlyPayment = 0f,
            warrantyCostsRate = 0.10f,
            notaryFeesRate = 3.5f,
            loanInterestRate = 4.0f
        )
    }

    @Test
    fun testTotalNotaryFees() {
        assertEquals(3500f, loan.totalNotaryFees, 0.01f)
    }

    @Test
    fun testMonthlyLoanPayment() {
        assertEquals(566.59f, loan.monthlyLoanPayment, 0.01f)
    }

    @Test
    fun testTotalWarrantyCosts() {
        assertEquals(1870f, loan.totalWarrantyCosts, 0.01f)
    }

    @Test
    fun testMonthlyWarrantyCosts() {
        assertEquals(7.79f, loan.monthlyWarrantyCosts, 0.01f)
    }

    @Test
    fun testTotalLoanInterest() {
        assertEquals(42482.06f, loan.totalLoanInterest, 0.01f)
    }

    @Test
    fun testDebtRate() {
        assertEquals(18.88f, loan.debtRate, 0.01f)
    }
}
