package com.waminiyi.realestatemanager.core.model

import org.threeten.bp.LocalDate

/**
 * Enumeration class representing different timeframes.
 * Each timeframe has a corresponding value string.
 * @property value The string representation of the timeframe.
 */
enum class Timeframe(val value: String) {
    LESS_THAN_ONE_WEEK("< 1 week"),
    LESS_THAN_ONE_MONTH("< 1 month"),
    LESS_THAN_THREE_MONTHS("< 3 months");

    companion object {
        /**
         * Returns the start date based on the timeframe.
         * @return The start date calculated based on the timeframe.
         */
        fun Timeframe.startDate(): LocalDate {
            return when (this) {
                LESS_THAN_ONE_WEEK -> LocalDate.now().minusWeeks(1)
                LESS_THAN_ONE_MONTH -> LocalDate.now().minusMonths(1)
                LESS_THAN_THREE_MONTHS -> LocalDate.now().minusMonths(3)
            }
        }
    }
}