package com.waminiyi.realestatemanager.core.sync.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.waminiyi.realestatemanager.core.sync.status.SyncSubscriber
import com.waminiyi.realestatemanager.core.data.datastore.model.VersionsList
import com.waminiyi.realestatemanager.core.data.datastore.repository.UserPreferencesRepository
import com.waminiyi.realestatemanager.core.data.remote.model.RemoteChange
import com.waminiyi.realestatemanager.core.data.remote.repository.RemoteDataRepository
import com.waminiyi.realestatemanager.core.data.repository.AgentRepository
import com.waminiyi.realestatemanager.core.data.repository.EstateRepository
import com.waminiyi.realestatemanager.core.data.repository.PhotoRepository
import com.waminiyi.realestatemanager.core.database.model.LocalChangeEntity
import com.waminiyi.realestatemanager.core.sync.initializers.SyncConstraints
import com.waminiyi.realestatemanager.core.sync.initializers.syncForegroundInfo
import com.waminiyi.realestatemanager.core.util.remdispatchers.Dispatcher
import com.waminiyi.realestatemanager.core.util.remdispatchers.RemDispatchers.IO
import com.waminiyi.realestatemanager.core.util.sync.Synchronizer
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

/**
 * Syncs the data layer by delegating to the appropriate repository instances with
 * sync functionality.
 */
@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val estateRepository: EstateRepository,
    private val agentRepository: AgentRepository,
    private val photoRepository: PhotoRepository,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val syncSubscriber: SyncSubscriber,
    private val remoteDataRepository: RemoteDataRepository
) : CoroutineWorker(appContext, workerParams), Synchronizer {

    override suspend fun getForegroundInfo(): ForegroundInfo =
        appContext.syncForegroundInfo()

    override suspend fun doWork(): Result = withContext(ioDispatcher) {

        syncSubscriber.subscribe()
        Log.d("SYNC", "suscribed")

        // First sync data to remote repositories
        val syncedSuccessfullyToRemote = awaitAll(
            async { photoRepository.syncToRemote() },
            async { estateRepository.syncToRemote() },
            async { agentRepository.syncToRemote() },
        ).all { it }

        Log.d("SYNC-FROM-REMOTE-STARTED", "SYNC-FROM-REMOTE-STARTED")
        // Then fetch remote changes
        val syncedSuccessfullyFromRemote =
            awaitAll(
                async { photoRepository.syncFromRemote() },
                async { estateRepository.syncFromRemote() },
                async { agentRepository.syncFromRemote() },
            ).all { it }

        val syncedSuccessfully = syncedSuccessfullyToRemote && syncedSuccessfullyFromRemote


        if (syncedSuccessfully) {
            Result.success()
        } else {
            Result.retry()
        }
    }

    override suspend fun getLocalVersionsList() = userPreferencesRepository.getLocalVersionsList()
    override suspend fun updateLocalVersionsList(
        update: VersionsList.() -> VersionsList
    ) = userPreferencesRepository.updateLocalVersionsList(update)

    override suspend fun updateRemoteChanges(
        update: (List<LocalChangeEntity>) -> List<RemoteChange>
    ) = remoteDataRepository.updateRemoteChanges(update)


    companion object {
        /**
         * Expedited one time work to sync data on app startup
         */
        fun startUpSyncWork() = OneTimeWorkRequestBuilder<SyncWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(SyncConstraints)
            .setInputData(SyncWorker::class.delegatedData())
            .build()
    }
}
