package com.waminiyi.realestatemanager.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.waminiyi.realestatemanager.core.database.RemDatabase
import com.waminiyi.realestatemanager.core.database.dao.AgentDao
import com.waminiyi.realestatemanager.core.database.dao.ImageDao
import com.waminiyi.realestatemanager.core.database.model.AgentEntity
import com.waminiyi.realestatemanager.core.database.model.ImageEntity
import com.waminiyi.realestatemanager.core.model.data.ImageType
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import java.util.*
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
        val agentUuid1: UUID = UUID.randomUUID()
        val agentUuid2: UUID = UUID.randomUUID()
        val agent1 = AgentEntity(agentUuid1, "John", "Doe", "agent@mail.com", "123-456")
        val agent2 = AgentEntity(agentUuid2, "James", "Bond", "agent@mail.com", "123-456")
        val image1 = ImageEntity(
            UUID.randomUUID(), agentUuid1,
            "image_name1", "description1", ImageType.MAIN
        )
        val image2 = ImageEntity(
            UUID.randomUUID(), agentUuid2,
            "image_name2", "description2", ImageType.MAIN
        )
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
        var agents = agentDao.getAllAgents().first()

        // Then: The database should contain no agents
        assertEquals(0, agents.size)

        // When: Inserting a new agent
        agentDao.upsertAgent(agent1)

        // Then: The database should contain one agent
        agents = agentDao.getAllAgents().first()
        assertEquals(1, agents.size)
        assertEquals(agent1, agents[0].agentEntity)
        assertEquals(image1, agents[0].imageEntity)

        // When: Updating the existing agent's phone number
        val updatedAgent = agent1.copy(phoneNumber = "012-012")
        agentDao.upsertAgent(updatedAgent)

        // Then: The database should still contain only one agent with the updated phone number
        agents = agentDao.getAllAgents().first()
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
        val retrievedAgent = agentDao.getAgent(agentUuid1).first()

        // Then: the retrieved agent should match the agent with the passed id
        assertNotNull(retrievedAgent)
        assertEquals(agent1, retrievedAgent?.agentEntity)
        assertEquals(image1, retrievedAgent?.imageEntity)
    }
}
