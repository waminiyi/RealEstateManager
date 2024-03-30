package com.waminiyi.realestatemanager.events

sealed class Event {
    data object HideRightFragment : Event()

    data class EstateClicked(val estateId: String) : Event()
    data class OpenEditFragment(val estateId: String?) : Event()

}