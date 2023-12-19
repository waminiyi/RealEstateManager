package com.waminiyi.realestatemanager.core.util.sync

import android.util.Log
import com.waminiyi.realestatemanager.core.data.datastore.model.LastCommit
import com.waminiyi.realestatemanager.core.data.datastore.model.VersionsList
import com.waminiyi.realestatemanager.core.data.remote.model.RemoteChange
import com.waminiyi.realestatemanager.core.database.model.LocalChangeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlin.coroutines.cancellation.CancellationException

/**
 * Interface marker for a class that manages synchronization between local data and a remote
 * source for a [Syncable].
 */
interface Synchronizer {
    suspend fun getLocalVersionsList(): VersionsList

    suspend fun updateLocalVersionsList(update: VersionsList.() -> VersionsList)

    suspend fun updateRemoteChanges(update: (List<LocalChangeEntity>) -> List<RemoteChange>)

    /**
     * Syntactic sugar to call [Syncable.syncFromRemoteWith] while omitting the synchronizer argument
     */
    suspend fun Syncable.syncFromRemote() = this@syncFromRemote.syncFromRemoteWith(this@Synchronizer)

    suspend fun Syncable.syncToRemote() = this@syncToRemote.syncToRemoteWith(this@Synchronizer)
}

/**
 * Interface marker for a class that is synchronized with a remote source. Syncing must not be
 * performed concurrently and it is the [Synchronizer]'s responsibility to ensure this.
 */
interface Syncable {
    /**
     * Synchronizes the local database backing the repository with the network.
     * Returns if the sync was successful or not.
     */
    suspend fun syncFromRemoteWith(synchronizer: Synchronizer): Boolean

    suspend fun syncToRemoteWith(synchronizer: Synchronizer): Boolean
}

/**
 * Attempts [block], returning a successful [Result] if it succeeds, otherwise a [Result.Failure]
 * taking care not to break structured concurrency
 */
private suspend fun <T> suspendRunCatching(block: suspend () -> T): Result<T> = try {
    Result.success(block())
} catch (cancellationException: CancellationException) {
    throw cancellationException
} catch (exception: Exception) {
    Log.i(
        "suspendRunCatching",
        "Failed to evaluate a suspendRunCatchingBlock. Returning failure Result",
        exception,
    )
    Result.failure(exception)
}

/**
 * Utility function for syncing a repository with the network.
 * [currentLocalVersionReader] Reads the current version of the model that needs to be synced
 * [remoteChangeListFetcher] Fetches the change list for the model
 * [localVersionUpdater] Updates the [LastCommit] after a successful sync
 * [localModelUpdater] Updates models by consuming the ids and the class tag of the models that have changed.
 *
 * Note that the blocks defined above are never run concurrently, and the [Synchronizer]
 * implementation must guarantee this.
 */
suspend fun Synchronizer.changeLocalListSync(
    currentLocalVersionReader: (VersionsList) -> Long,
    remoteChangeListFetcher: suspend (Long) -> List<RemoteChange>,
    localVersionUpdater: VersionsList.(Long) -> VersionsList,
    localModelDeleter: (suspend (List<String>) -> Unit)? = null,
    localModelUpdater: suspend (List<String>) -> Unit,
) = suspendRunCatching {
    // Fetch the change list since last sync (akin to a git fetch)
    val currentVersion = currentLocalVersionReader(getLocalVersionsList())
    val changeList = remoteChangeListFetcher(currentVersion)
    if (changeList.isEmpty()) return@suspendRunCatching true

    val (deleted, updated) = changeList.partition(RemoteChange::isDeleted)

    localModelDeleter?.let { deleter -> deleter(deleted.map(RemoteChange::id)) }

    // Using the change list, pull down and save the changes (akin to a git pull)
    localModelUpdater(updated.map(RemoteChange::id))

    // Update the last synced version (akin to updating local git HEAD)
    val latestVersion = changeList.last().version
    updateLocalVersionsList {
        localVersionUpdater(latestVersion)
    }
}.isSuccess

suspend fun Synchronizer.changeRemoteListSync(
    localChangesFetcher: suspend () -> List<LocalChangeEntity>,
    localVersionUpdater: VersionsList.(Long) -> VersionsList,
    remoteVersionUpdater: (List<LocalChangeEntity>, Long) -> List<RemoteChange>,
    remoteModelDeleter: (suspend (List<String>) -> Unit)? = null,
    remoteModelUpdater: suspend (List<String>) -> Unit,
) = suspendRunCatching {
    val localChanges = localChangesFetcher()
    if (localChanges.isEmpty()) return@suspendRunCatching true
    val (deleted, updated) = localChanges.partition(LocalChangeEntity::isDeleted)

    remoteModelDeleter.let { (deleted.map(LocalChangeEntity::id)) }
    remoteModelUpdater(updated.map(LocalChangeEntity::id))

    // Update the last synced version (akin to updating local git HEAD)
    val latestVersion = System.currentTimeMillis()
    updateLocalVersionsList {
        localVersionUpdater(latestVersion)
    }
    updateRemoteChanges {
        remoteVersionUpdater(localChanges, latestVersion)
    }
}.isSuccess

/**
 * Returns a [Flow] whose values are generated by [transform] function that process the most
 * recently emitted values by each flow.
 */
fun <T1, T2, T3, T4, T5, T6, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    transform: suspend (T1, T2, T3, T4, T5, T6) -> R,
): Flow<R> = combine(
    combine(flow, flow2, flow3, ::Triple),
    combine(flow4, flow5, flow6, ::Triple),
) { t1, t2 ->
    transform(
        t1.first,
        t1.second,
        t1.third,
        t2.first,
        t2.second,
        t2.third,
    )
}