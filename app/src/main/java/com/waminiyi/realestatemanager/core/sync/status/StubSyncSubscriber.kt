

package com.waminiyi.realestatemanager.core.sync.status

import android.util.Log
import com.google.samples.apps.nowinandroid.sync.status.SyncSubscriber
import javax.inject.Inject

private const val TAG = "StubSyncSubscriber"

/**
 * Stub implementation of [SyncSubscriber]
 */
class StubSyncSubscriber @Inject constructor() : SyncSubscriber {
    override suspend fun subscribe() {
        Log.d(TAG, "Subscribing to sync")
    }
}
