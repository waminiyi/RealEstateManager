package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.model.data.Filter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class FilterRepository @Inject constructor() {

    private val defaultFilter = Filter()
    private val _filter = MutableStateFlow(defaultFilter)
    val filter: Flow<Filter> = _filter

    fun updateFilter(filter: Filter) {
        _filter.value = filter
    }

    fun resetFilter() {
        _filter.value = defaultFilter
    }
}