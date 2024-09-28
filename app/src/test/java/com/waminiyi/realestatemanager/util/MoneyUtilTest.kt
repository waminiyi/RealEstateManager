package com.waminiyi.realestatemanager.util

import com.waminiyi.realestatemanager.util.util.formatAsEuro
import com.waminiyi.realestatemanager.util.util.formatAsUSDollar
import com.waminiyi.realestatemanager.util.util.toEuro
import com.waminiyi.realestatemanager.util.util.toUSDollar
import org.junit.Assert.assertEquals
import org.junit.Test

class MoneyUtilTest {
    @Test
    fun testConversionFromUSDtoEuro() {
        val usdAmount = 100
        val expectedEuroAmount = 94

        val actualEuroAmount = usdAmount.toEuro()

        assertEquals(expectedEuroAmount, actualEuroAmount)
    }

    @Test
    fun testConversionFromEuroToUSD() {
        val euroAmount = 100
        val expectedUSDAmount = 106
        val actualUSDAmount = euroAmount.toUSDollar()

        assertEquals(expectedUSDAmount, actualUSDAmount)
    }

    private fun removeNonBreakingSpace(input: String): String {
        return input.replace("\u00A0", " ")
    }

    @Test
    fun testFormatAsUSDollar() {
        val amount = 100
        val usdFormatted = removeNonBreakingSpace(amount.formatAsUSDollar())
        val expectedResult = "$100.00"

        assertEquals(expectedResult, usdFormatted)
    }

    @Test
    fun testFormatAsEuro() {
        val amount = 100
        val euroFormatted = removeNonBreakingSpace(amount.formatAsEuro())
        val expectedResult = "100,00 €"

        assertEquals(expectedResult, euroFormatted)
    }
}