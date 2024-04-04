package com.waminiyi.realestatemanager.data

import com.waminiyi.realestatemanager.core.data.repository.FilterRepository
import com.waminiyi.realestatemanager.core.model.EstateStatus
import com.waminiyi.realestatemanager.core.model.EstateType
import com.waminiyi.realestatemanager.core.model.Filter
import com.waminiyi.realestatemanager.core.model.PointOfInterest
import com.waminiyi.realestatemanager.core.model.Timeframe
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FilterRepositoryTest {
    private lateinit var filterRepository: FilterRepository
    private lateinit var newFilter: Filter

    @Before
    fun setUp() {
        filterRepository = FilterRepository()
        newFilter = Filter(
            estateTypes = listOf(EstateType.APARTMENT, EstateType.HOUSE),
            minPrice = 100000,
            maxPrice = 300000,
            minArea = 50,
            maxArea = 200,
            roomsCounts = listOf(2, 3),
            bedroomsCounts = listOf(1, 2, 3),
            photosMinimalCount = 3,
            cities = listOf("New York", "Los Angeles"),
            pointOfInterest = PointOfInterest.PARK,
            estateStatus = EstateStatus.AVAILABLE,
            entryDateTimeframe = Timeframe.LESS_THAN_ONE_MONTH,
            saleDateTimeframe = Timeframe.LESS_THAN_ONE_WEEK
        )
    }

    @Test
    fun ` test filter is correctly updated and reset`() = runBlocking {

        // When nothing is done, we have the default filter
        filterRepository.filter.take(1).collect { filter ->
            assertEquals(Filter(), filter)
        }

        // When we update the filter
        filterRepository.updateFilter(newFilter)

        // The filter should have been update
        filterRepository.filter.take(1).collect { filter ->
            assertEquals(newFilter, filter)
        }

        // When we reset the filter
        filterRepository.resetFilter()

        // The current filter should be the default
        filterRepository.filter.take(1).collect { filter ->
            assertEquals(Filter(), filter)
        }

    }

}