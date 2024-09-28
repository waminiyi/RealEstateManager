package com.waminiyi.realestatemanager.data

import com.waminiyi.realestatemanager.data.repository.DefaultAgentRepository
import com.waminiyi.realestatemanager.data.database.dao.AgentDao
import com.waminiyi.realestatemanager.data.database.model.AgentEntity
import com.waminiyi.realestatemanager.data.models.Result
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okio.IOException
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.UUID

class DefaultAgentRepositoryTest {

    private lateinit var agentDao: AgentDao
    private lateinit var repository: DefaultAgentRepository
    private lateinit var agentEntity: AgentEntity

    @Before
    fun setUp() {
        agentDao = mockk()
        repository = DefaultAgentRepository(agentDao)
        agentEntity = AgentEntity(
            agentUuid = UUID.fromString("51f66076-ef9b-4482-90d7-0869a472cc48"),
            firstName = "James",
            lastName = "Bond",
            email = "jamesbond007@example.com",
            phoneNumber = "0123456789",
            photoUrl = "photourl.com",
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `saveAgent in repository should call upsertAgent in AgentDao successfully`() = runBlocking {
        // Given
        coEvery { agentDao.upsertAgent(any()) } returns Unit

        // When
        val result = repository.saveAgent(agentEntity.asAgent())

        // Then
        assert(result is com.waminiyi.realestatemanager.data.models.DataResult.Result.Success)
        coVerify(exactly = 1) { agentDao.upsertAgent(agentEntity) }
    }

    @Test
    @Throws(IOException::class)
    fun `saveAgent should return error when exception occurs`() = runBlocking {
        // Given
        coEvery { agentDao.upsertAgent(any()) } throws IOException()

        // When
        val result = repository.saveAgent(agentEntity.asAgent())

        // Then
        assert(result is com.waminiyi.realestatemanager.data.models.DataResult.Result.Error)
        coVerify(exactly = 1) { agentDao.upsertAgent(agentEntity) }
    }

    @Test
    fun `getAllAgents in repository should call getAllAgents in AgentDao `() = runBlocking {
        // Given
        val agentEntities = listOf(agentEntity, agentEntity.copy(agentUuid = UUID.randomUUID()))
        coEvery { agentDao.getAllAgents() } returns agentEntities

        // When
        val result = repository.getAllAgents()

        // Then
        assert(result is com.waminiyi.realestatemanager.data.models.DataResult.Result.Success)
        assert((result as com.waminiyi.realestatemanager.data.models.DataResult.Result.Success).data.size == agentEntities.size)
        coVerify(exactly = 1) { agentDao.getAllAgents() }
    }

    @Test
    @Throws(IOException::class)
    fun `getAllAgents should return error when exception occurs`() = runBlocking {
        // Given
        coEvery { agentDao.getAllAgents() } throws IOException()

        // When
        val result = repository.getAllAgents()

        // Then
        assert(result is com.waminiyi.realestatemanager.data.models.DataResult.Result.Error)
        coVerify(exactly = 1) { agentDao.getAllAgents() }
    }

    @Test
    fun `getAgent  in repository should call getAgent in AgentDao`() = runBlocking {
        // Given
        coEvery { agentDao.getAgent(agentEntity.agentUuid) } returns agentEntity

        // When
        val result = repository.getAgent(agentEntity.agentUuid.toString())

        // Then
        assertEquals(Result.Success(agentEntity.asAgent()), result)
        coVerify(exactly = 1) { agentDao.getAgent(agentEntity.agentUuid) }
    }
}