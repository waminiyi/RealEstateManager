

package com.waminiyi.realestatemanager.core.sync.status

import com.google.firebase.messaging.FirebaseMessaging
import com.google.samples.apps.nowinandroid.sync.initializers.SYNC_TOPIC
import com.google.samples.apps.nowinandroid.sync.status.SyncSubscriber
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Implementation of [SyncSubscriber] that subscribes to the FCM [SYNC_TOPIC]
 */
class FirebaseSyncSubscriber @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging,
) : SyncSubscriber {
    override suspend fun subscribe() {
        firebaseMessaging
            .subscribeToTopic(SYNC_TOPIC)
            .await()
    }
}