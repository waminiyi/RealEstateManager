package com.waminiyi.realestatemanager.core.util.util

import com.waminiyi.realestatemanager.core.Constants.HUNDRED
import com.waminiyi.realestatemanager.core.util.util.CurrencyCode.EUR
import com.waminiyi.realestatemanager.core.util.util.CurrencyCode.USD
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import kotlin.math.round

/**
 * Converts numerical value from USD to EUR
 */
fun Int.toEuro(): Int = round(this * USD.rateToDollar / EUR.rateToDollar).toInt()

/**
 * Converts numerical value from EUR to USD
 */
fun Int.toUSDollar(): Int = round(this * EUR.rateToDollar).toInt()

/**
 * @return A formatted currency string representing the US Dollar
 *
 */
fun Int.formatAsUSDollar() = formatCurrency(USD)

/**
 * @return A formatted currency string representing the Euro
 *
 */
fun Int.formatAsEuro() = formatCurrency(EUR)

/**
 * @return A formatted currency string representing the given numeric value and currency code.
 * @param currencyCode The ISO 4217 currency code (e.g., "USD", "EUR", "GBP").
 */
private fun Int.formatCurrency(currencyCode: CurrencyCode): String {
    val locale = when (currencyCode) {
        USD -> Locale.US
        EUR -> Locale.FRANCE
    }
    val currencyInstance = Currency.getInstance(currencyCode.name)
    val format = NumberFormat.getCurrencyInstance(locale)
    format.currency = currencyInstance
    return format.format(this)
}

/**
 * Currencies Codes with their conversion rate to US Dollar
 */
enum class CurrencyCode(val rateToDollar: Float) {
    USD(1F),
    EUR(1.06F)
}

fun priceToText(priceInDollars: Int, currencyCode: CurrencyCode): String = when (currencyCode) {
    USD -> priceInDollars.formatAsUSDollar()
    EUR -> priceInDollars.toEuro().formatAsEuro()
}

fun Float.roundToTwoDigits(): Float = (this * HUNDRED).toInt() / HUNDRED.toFloat()