package com.waminiyi.realestatemanager.database.dao

import com.waminiyi.realestatemanager.core.database.dao.AgentDao
import com.waminiyi.realestatemanager.core.database.dao.ImageDao
import com.waminiyi.realestatemanager.core.model.data.ImageType
import com.waminiyi.realestatemanager.database.TestDataGenerator
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class AgentDaoTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var agentDao: AgentDao

    @Inject
    lateinit var imageDao: ImageDao

    companion object {
        val agent1 = TestDataGenerator.getRandomAgent()
        val agent2 = TestDataGenerator.getRandomAgent()
        val image1 = TestDataGenerator.getRandomImage(agent1.agentUuid, ImageType.MAIN)
        val image2 = TestDataGenerator.getRandomImage(agent2.agentUuid, ImageType.MAIN)
    }

    @Before
    fun setUp() = runBlocking {
        hiltRule.inject()
        imageDao.upsertImage(image1)
        imageDao.upsertImage(image2)
    }

    @Test
    fun upsertAndGetAllAgentsTest() = runBlocking {
        // Given: No agent added to the database
        var agents = agentDao.getAllAgents()

        // Then: The database should contain no agents
        assertEquals(0, agents.size)

        // When: Inserting a new agent
        agentDao.upsertAgent(agent1)

        // Then: The database should contain one agent
        agents = agentDao.getAllAgents()
        assertEquals(1, agents.size)
        assertEquals(agent1, agents[0].agentEntity)
        assertEquals(image1, agents[0].imageEntity)

        // When: Updating the existing agent's phone number
        val updatedAgent = agent1.copy(phoneNumber = "012-012")
        agentDao.upsertAgent(updatedAgent)

        // Then: The database should still contain only one agent with the updated phone number
        agents = agentDao.getAllAgents()
        assertEquals(1, agents.size)
        assertEquals(agent1.agentUuid, agents[0].agentEntity.agentUuid)
        assertEquals(agent1.firstName, agents[0].agentEntity.firstName)
        assertEquals(updatedAgent.phoneNumber, agents[0].agentEntity.phoneNumber)
        assertEquals(image1, agents[0].imageEntity)

    }

    /**
     * Test the getAgent function of AgentDao.
     */
    @Test
    fun getAgentByIdTest() = runBlocking {
        // Given: some agents in the database
        agentDao.upsertAgent(agent1)
        agentDao.upsertAgent(agent2)

        // When: retrieving an agent by ID from the database
        val retrievedAgent = agentDao.getAgent(agent1.agentUuid)

        // Then: the retrieved agent should match the agent with the passed id
        assertNotNull(retrievedAgent)
        assertEquals(agent1, retrievedAgent?.agentEntity)
        assertEquals(image1, retrievedAgent?.imageEntity)
    }
}
