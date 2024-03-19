package com.waminiyi.realestatemanager.core.database.model

import com.waminiyi.realestatemanager.core.model.data.EstateStatus
import com.waminiyi.realestatemanager.core.model.data.EstateType
import com.waminiyi.realestatemanager.core.model.data.PointOfInterest
import java.util.Date

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