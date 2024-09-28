package com.waminiyi.realestatemanager.presentation.estatesFilter

import com.waminiyi.realestatemanager.Constants.FILTER_ROOM_COUNT_THRESHOLD
import com.waminiyi.realestatemanager.data.models.Filter

data class FilterUiState(
    val filter: Filter = Filter(),
    val usCities: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadedUiState: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null,
)

fun Int.asFilterRoomCountText(): String = when (this) {
    FILTER_ROOM_COUNT_THRESHOLD -> "${this}+"
    else -> this.toString()
}
