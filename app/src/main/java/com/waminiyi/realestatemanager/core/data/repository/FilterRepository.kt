package com.waminiyi.realestatemanager.core.data.repository

import com.waminiyi.realestatemanager.core.model.data.Filter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class FilterRepository @Inject constructor() {

    private val defaultFilter = Filter()
    private val _filter = MutableStateFlow(defaultFilter)
    val filter: Flow<Filter> = _filter
    private val _isDefaultFilter = MutableStateFlow(_filter.value.isDefault())
    val isDefaultFilter: Flow<Boolean> = _isDefaultFilter


    fun updateFilter(filter: Filter) {
        _filter.value = filter
        _isDefaultFilter.value = filter.isDefault()
    }

    fun resetFilter() {
        updateFilter(defaultFilter)
    }
}