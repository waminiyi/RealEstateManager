package com.waminiyi.realestatemanager.database.dao

import com.waminiyi.realestatemanager.core.database.dao.AgentDao
import com.waminiyi.realestatemanager.core.database.dao.EstateDao
import com.waminiyi.realestatemanager.core.database.dao.ImageDao
import com.waminiyi.realestatemanager.core.model.data.ImageType
import com.waminiyi.realestatemanager.core.model.data.Status
import com.waminiyi.realestatemanager.database.TestDataGenerator
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import javax.inject.Inject

@HiltAndroidTest
class EstateDaoTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var estateDao: EstateDao

    @Inject
    lateinit var imageDao: ImageDao

    @Inject
    lateinit var agentDao: AgentDao

    companion object {
        val estateUuid1: UUID = UUID.randomUUID()
        val estateUuid2: UUID = UUID.randomUUID()
        val mainImageUuid1: UUID = UUID.randomUUID()
        val image1 = TestDataGenerator.getRandomImage(estateUuid1, ImageType.MAIN)
        val image2 = TestDataGenerator.getRandomImage(estateUuid2, ImageType.MAIN)
        val image3 = TestDataGenerator.getRandomImage(estateUuid1, ImageType.ADDITIONAL)
        val agent1 = TestDataGenerator.getRandomAgent()
        val estate1 = TestDataGenerator.getRandomEstate(estateUuid1, image1.imageUuid, agent1.agentUuid)
        val estate2 = TestDataGenerator.getRandomEstate(estateUuid2, image2.imageUuid, agent1.agentUuid)
    }

    @Before
    fun setUp() = runBlocking {
        hiltRule.inject()
        agentDao.upsertAgent(agent1)
        imageDao.upsertImage(image1)
        imageDao.upsertImage(image2)
        imageDao.upsertImage(image3)
    }

    @Test
    fun upsertAndGetAllEstatesWithImagesTest() = runBlocking {
        // Given: No estates added to the database
        var estatesWithImages = estateDao.getAllEstatesWithImages().first()

        // Then: The database should contain no estates
        assertEquals(0, estatesWithImages.size)

        // When: Inserting a new estate
        estateDao.upsertEstate(estate1)

        // Then: The database should contain one estate
        estatesWithImages = estateDao.getAllEstatesWithImages().first()
        assertEquals(1, estatesWithImages.size)
        assertEquals(estate1, estatesWithImages[0].estateEntity)
        assertEquals(image1, estatesWithImages[0].imageEntity)

        // When: Inserting another estate
        estateDao.upsertEstate(estate2)

        // Then: The database should contain two estates
        estatesWithImages = estateDao.getAllEstatesWithImages().first()
        assertEquals(2, estatesWithImages.size)
        assertEquals(estate1, estatesWithImages[0].estateEntity)
        assertEquals(estate2, estatesWithImages[1].estateEntity)

        // When: Updating existing estate
        val updatedEstate = estate1.copy(status = Status.SOLD)
        estateDao.upsertEstate(updatedEstate)

        // Then: The database should contain two estates and the target estate should be up to date
        estatesWithImages = estateDao.getAllEstatesWithImages().first()
        assertEquals(2, estatesWithImages.size)
        assertEquals(estate1.estateUuid, estatesWithImages[0].estateEntity.estateUuid)
        assertEquals(estate1.type, estatesWithImages[0].estateEntity.type)
        assertEquals(Status.SOLD, estatesWithImages[0].estateEntity.status)
    }

    @Test
    fun getEstateWithDetailsByIdTest() = runBlocking {
        // Given: some estates in the database
        estateDao.upsertEstate(estate1)
        estateDao.upsertEstate(estate2)

        // When: retrieving an estate with details by ID from the database
        val retrievedEstate = estateDao.getEstateWithDetailsById(estateUuid1)

        // Then: the retrieved estate should match the estate with the passed id
        assertNotNull(retrievedEstate)
        assertEquals(estate1, retrievedEstate?.estateEntity)
    }
}
