package com.waminiyi.realestatemanager.core.model.data

import com.waminiyi.realestatemanager.core.database.model.FilterQueryParameter
import com.waminiyi.realestatemanager.core.model.data.Timeframe.Companion.startDate
import org.threeten.bp.DateTimeUtils
import org.threeten.bp.ZoneId

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
    fun isDefault() = this == Filter()
    fun asQueryParameter(): FilterQueryParameter {
        return FilterQueryParameter(
            minPrice = minPrice,
            maxPrice = maxPrice,
            minArea = minArea,
            maxArea = maxArea,
            typesIsEmpty = estateTypes.isNullOrEmpty(),
            estateTypes = estateTypes.ifEmpty { null },
            roomsIsEmpty = roomsCounts.isNullOrEmpty(),
            roomsCounts = roomsCounts.ifEmpty { null },
            roomsCountThreshold = if (roomsCounts.contains(5)) 5 else null,
            bedroomsIsEmpty = bedroomsCounts.isNullOrEmpty(),
            bedroomsCounts = bedroomsCounts.ifEmpty { null },
            bedroomsCountThreshold = if (bedroomsCounts.contains(5)) 5 else null,
            photosMinimalCount = photosMinimalCount,
            citiesIsEmpty = cities.isNullOrEmpty(),
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
