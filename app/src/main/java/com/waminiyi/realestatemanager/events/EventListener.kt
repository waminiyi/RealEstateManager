package com.waminiyi.realestatemanager.events

interface EventListener {
    fun onEvent(event: Event)
}