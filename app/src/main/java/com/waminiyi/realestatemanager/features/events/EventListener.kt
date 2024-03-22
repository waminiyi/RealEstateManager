package com.waminiyi.realestatemanager.features.events

interface EventListener {
    fun onEvent(event: Event)
}