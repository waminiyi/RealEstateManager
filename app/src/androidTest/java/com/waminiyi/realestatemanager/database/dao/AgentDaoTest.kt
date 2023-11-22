package com.waminiyi.realestatemanager.database.dao

import com.waminiyi.realestatemanager.core.database.dao.AgentDao
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

    companion object {
        val agent1 = TestDataGenerator.getRandomAgent()
        val agent2 = TestDataGenerator.getRandomAgent()
    }

    @Before
    fun setUp() = runBlocking {
        hiltRule.inject()
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
        assertEquals(agent1, agents[0])

        // When: Updating the existing agent's phone number
        val updatedAgent = agent1.copy(phoneNumber = "012-012")
        agentDao.upsertAgent(updatedAgent)

        // Then: The database should still contain only one agent with the updated phone number
        agents = agentDao.getAllAgents()
        assertEquals(1, agents.size)
        assertEquals(agent1.agentUuid, agents[0].agentUuid)
        assertEquals(agent1.firstName, agents[0].firstName)
        assertEquals(updatedAgent.phoneNumber, agents[0].phoneNumber)
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
        assertEquals(agent1, retrievedAgent)
    }
}
