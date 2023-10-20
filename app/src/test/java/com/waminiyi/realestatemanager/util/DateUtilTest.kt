package com.waminiyi.realestatemanager.util

import com.waminiyi.realestatemanager.core.util.util.currentDate
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class DateUtilTest {
    @Test
    fun testGetCurrentDate() {
        val todayDate = LocalDate.now()
        val expectedDate = "${todayDate.dayOfMonth}/${todayDate.monthValue}/${todayDate.year}"

        assertEquals(expectedDate, currentDate)
    }
}