package com.waminiyi.realestatemanager.core.sync.workers

import com.waminiyi.realestatemanager.analytics.AnalyticsEvent
import com.waminiyi.realestatemanager.analytics.AnalyticsHelper

fun AnalyticsHelper.logSyncStarted() =
    logEvent(
        AnalyticsEvent(type = "network_sync_started"),
    )

fun AnalyticsHelper.logSyncFinished(syncedSuccessfully: Boolean) {
    val eventType = if (syncedSuccessfully) "network_sync_successful" else "network_sync_failed"
    logEvent(
        AnalyticsEvent(type = eventType),
    )
}
