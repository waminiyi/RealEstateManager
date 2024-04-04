package com.waminiyi.realestatemanager.core.events

interface EventListener {
    fun onEvent(event: Event)
}