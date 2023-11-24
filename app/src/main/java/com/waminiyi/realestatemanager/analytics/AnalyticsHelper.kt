package com.waminiyi.realestatemanager.analytics

/**
 * Interface for logging analytics events. See `StubAnalyticsHelper` for implementations.
 */
interface AnalyticsHelper {
    fun logEvent(event: AnalyticsEvent)
}