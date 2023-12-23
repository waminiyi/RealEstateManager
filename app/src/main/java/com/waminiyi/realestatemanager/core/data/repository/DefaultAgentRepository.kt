package com.waminiyi.realestatemanager.core.data.repository

import android.util.Log
import com.waminiyi.realestatemanager.core.data.datastore.model.VersionsList
import com.waminiyi.realestatemanager.core.data.extension.toAgentEntity
import com.waminiyi.realestatemanager.core.data.extension.toRemoteAgent
import com.waminiyi.realestatemanager.core.data.remote.model.RemoteChange
import com.waminiyi.realestatemanager.core.data.remote.repository.RemoteDataRepository
import com.waminiyi.realestatemanager.core.database.dao.AgentDao
import com.waminiyi.realestatemanager.core.database.dao.LocalChangeDao
import com.waminiyi.realestatemanager.core.database.model.AgentEntity
import com.waminiyi.realestatemanager.core.database.model.LocalChangeEntity
import com.waminiyi.realestatemanager.core.database.model.asAgentEntity
import com.waminiyi.realestatemanager.core.model.data.Agent
import com.waminiyi.realestatemanager.core.model.data.ClassTag
import com.waminiyi.realestatemanager.core.model.data.DataResult
import com.waminiyi.realestatemanager.core.util.sync.Synchronizer
import com.waminiyi.realestatemanager.core.util.sync.changeLocalListSync
import com.waminiyi.realestatemanager.core.util.sync.changeRemoteListSync
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class DefaultAgentRepository @Inject constructor(
    private val agentDao: AgentDao,
    private val localChangeDao: LocalChangeDao,
    private val remoteDataRepository: RemoteDataRepository
) : AgentRepository {
    override suspend fun saveAgent(agent: Agent): DataResult<Unit> {
        return try {
            agentDao.upsertAgent(agent.asAgentEntity())
            localChangeDao.upsertChange(LocalChangeEntity(agent.uuid, ClassTag.Agent, false))
            DataResult.Success(Unit)
        } catch (exception: IOException) {
            DataResult.Error(exception)
        }
    }

    override fun getAllAgents(): DataResult<List<Agent>> {
        return try {
            val result = agentDao.getAllAgents().map {
                it.asAgent()
            }
            DataResult.Success(result)
        } catch (exception: IOException) {
            DataResult.Error(exception)
        }
    }

    override fun getAgent(agentUuid: String): DataResult<Agent?> {
        return try {
            val result = agentDao.getAgent(UUID.fromString(agentUuid))?.asAgent()
            result?.let {
                DataResult.Success(it)
            } ?: DataResult.Error(NullPointerException("Agent with ID $agentUuid not found"))
        } catch (exception: IOException) {
            DataResult.Error(exception)
        }
    }

    override suspend fun getAgentsToUpload(): List<AgentEntity> = agentDao.getAgentsByIds(
        localChangeDao.getChangesByClassTag(ClassTag.Agent).map { change ->
            UUID.fromString(change.id)
        })


    override suspend fun syncFromRemoteWith(synchronizer: Synchronizer): Boolean {
        return synchronizer.changeLocalListSync(
            currentLocalVersionReader = VersionsList::agentVersion,
            remoteChangeListFetcher = { currentVersion -> remoteDataRepository.getAgentsChangeList(currentVersion) },
            localVersionUpdater = { latestVersion -> copy(agentVersion = latestVersion) },
            localModelUpdater = { changedIds ->
                changedIds.forEach { id ->
                    remoteDataRepository.getAgent(id)?.let {
                        Log.d("SYNC-FROM-REMOTE", it.toString())
                        agentDao.upsertAgent(it.toAgentEntity())
                        Log.d("SAVED-TO-LOCAL", it.toAgentEntity().toString())}
                }
            }
        )
    }

    override suspend fun syncToRemoteWith(synchronizer: Synchronizer): Boolean {
        return synchronizer.changeRemoteListSync(
            localChangesFetcher = { localChangeDao.getChangesByClassTag(ClassTag.Agent) },
            localVersionUpdater = { latestVersion -> copy(agentVersion = latestVersion) },
            remoteVersionUpdater = { localChanges, latestVersion ->
                localChanges.map {
                    RemoteChange(it.id, it.classTag.name, latestVersion, it.isDeleted)
                }
            },
            remoteModelUpdater = { changedIds ->
                changedIds.forEach { agentId ->
                    agentDao.getAgent(UUID.fromString(agentId))?.let {
                        remoteDataRepository.uploadAgent(it.toRemoteAgent())
                    }
                }
            }
        )
    }
}