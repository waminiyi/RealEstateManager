package com.waminiyi.realestatemanager.data.database.model

import com.waminiyi.realestatemanager.data.models.EstateStatus
import com.waminiyi.realestatemanager.data.models.EstateType
import com.waminiyi.realestatemanager.data.models.PointOfInterest
import java.util.Date

/**
 * Data class representing the filter parameters for querying real estate properties.
 *
 * This class encapsulates various parameters used to filter real estate properties in a query.
 *
 * @property minPrice The minimum price of the property.
 * @property maxPrice The maximum price of the property.
 * @property minArea The minimum area of the property.
 * @property maxArea The maximum area of the property.
 * @property typesIsEmpty Indicates whether the estate types list is empty.
 * @property estateTypes The list of estate types to filter.
 * @property roomsIsEmpty Indicates whether the rooms counts list is empty.
 * @property roomsCounts The list of room counts to filter.
 * @property roomsCountThreshold The threshold value for room counts.
 * @property bedroomsIsEmpty Indicates whether the bedrooms counts list is empty.
 * @property bedroomsCounts The list of bedroom counts to filter.
 * @property bedroomsCountThreshold The threshold value for bedroom counts.
 * @property photosMinimalCount The minimum count of photos for a property.
 * @property citiesIsEmpty Indicates whether the cities list is empty.
 * @property cities The list of cities to filter.
 * @property pointOfInterest The point of interest to filter.
 * @property estateStatus The status of the estate to filter.
 * @property addedAfter The date after which the property was added.
 * @property soldAfter The date after which the property was sold.
 */
data class FilterQueryParameter(
    val minPrice: Int? = null,
    val maxPrice: Int? = null,
    val minArea: Int? = null,
    val maxArea: Int? = null,
    val typesIsEmpty: Boolean = false,
    val estateTypes: List<EstateType>? = null,
    val roomsIsEmpty: Boolean = false,
    val roomsCounts: List<Int>? = null,
    val roomsCountThreshold: Int? = null,
    val bedroomsIsEmpty: Boolean = false,
    val bedroomsCounts: List<Int>? = null,
    val bedroomsCountThreshold: Int? = null,
    val photosMinimalCount: Int = 1,
    val citiesIsEmpty: Boolean = false,
    val cities: List<String>? = null,
    val pointOfInterest: PointOfInterest? = null,
    val estateStatus: EstateStatus? = null,
    val addedAfter: Date? = null,
    val soldAfter: Date? = null,
)