package com.waminiyi.realestatemanager.core.model

import com.waminiyi.realestatemanager.core.Constants.FILTER_ROOM_COUNT_THRESHOLD
import com.waminiyi.realestatemanager.core.database.model.FilterQueryParameter
import com.waminiyi.realestatemanager.core.model.Timeframe.Companion.startDate
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.ZoneId

/**
 * Data class representing a filter used for filtering estates.
 * @property estateTypes The list of estate types to filter by. Default is an empty list.
 * @property minPrice The minimum price filter. Default is null.
 * @property maxPrice The maximum price filter. Default is null.
 * @property minArea The minimum area filter. Default is null.
 * @property maxArea The maximum area filter. Default is null.
 * @property roomsCounts The list of rooms counts to filter by. Default is an empty list.
 * @property bedroomsCounts The list of bedrooms counts to filter by. Default is an empty list.
 * @property photosMinimalCount The minimal count of photos filter. Default is 1.
 * @property cities The list of cities to filter by. Default is an empty list.
 * @property pointOfInterest The point of interest to filter by. Default is null.
 * @property estateStatus The estate status to filter by. Default is null.
 * @property entryDateTimeframe The timeframe for entry date filter. Default is null.
 * @property saleDateTimeframe The timeframe for sale date filter. Default is null.
 */
data class Filter(
    val estateTypes: List<EstateType> = emptyList(),
    val minPrice: Int? = null,
    val maxPrice: Int? = null,
    val minArea: Int? = null,
    val maxArea: Int? = null,
    val roomsCounts: List<Int> = emptyList(),
    val bedroomsCounts: List<Int> = emptyList(),
    val photosMinimalCount: Int = 1,
    val cities: List<String> = emptyList(),
    val pointOfInterest: PointOfInterest? = null,
    val estateStatus: EstateStatus? = null,
    val entryDateTimeframe: Timeframe? = null,
    val saleDateTimeframe: Timeframe? = null,
) {

    /**
     * Checks if the filter is the default filter (all properties are their default values).
     * @return true if the filter is the default filter, false otherwise.
     */
    fun isDefault() = this == Filter()

    /**
     * Converts the filter to a query parameter representation.
     * @return The query parameter representation of the filter.
     */
    fun asQueryParameter(): FilterQueryParameter {
        return FilterQueryParameter(
            minPrice = minPrice,
            maxPrice = maxPrice,
            minArea = minArea,
            maxArea = maxArea,
            typesIsEmpty = estateTypes.isEmpty(),
            estateTypes = estateTypes.ifEmpty { null },
            roomsIsEmpty = roomsCounts.isEmpty(),
            roomsCounts = roomsCounts.ifEmpty { null },
            roomsCountThreshold = if (roomsCounts.contains(FILTER_ROOM_COUNT_THRESHOLD)) {
                FILTER_ROOM_COUNT_THRESHOLD
            } else null,
            bedroomsIsEmpty = bedroomsCounts.isEmpty(),
            bedroomsCounts = bedroomsCounts.ifEmpty { null },
            bedroomsCountThreshold = if (bedroomsCounts.contains(FILTER_ROOM_COUNT_THRESHOLD)) {
                FILTER_ROOM_COUNT_THRESHOLD
            } else null,
            photosMinimalCount = photosMinimalCount,
            citiesIsEmpty = cities.isEmpty(),
            cities = cities.ifEmpty { null },
            pointOfInterest = pointOfInterest,
            estateStatus = estateStatus,
            addedAfter = entryDateTimeframe?.let {
                DateTimeUtils.toDate(
                    it.startDate().atStartOfDay(ZoneId.systemDefault()).toInstant()
                )
            },
            soldAfter = saleDateTimeframe?.let {
                DateTimeUtils.toDate(
                    it.startDate().atStartOfDay(ZoneId.systemDefault()).toInstant()
                )
            },
        )
    }
}
