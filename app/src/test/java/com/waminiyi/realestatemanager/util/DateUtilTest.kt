package com.waminiyi.realestatemanager.util

import com.waminiyi.realestatemanager.core.utils.date.currentDate
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateUtilTest {
    @Test
    fun testGetCurrentDate() {
        val todayDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy") // Use your desired date format

        val expectedDate = todayDate.format(formatter)

        assertEquals(expectedDate, currentDate)
    }
}