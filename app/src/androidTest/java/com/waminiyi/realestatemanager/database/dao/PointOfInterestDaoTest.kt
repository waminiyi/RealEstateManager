package com.waminiyi.realestatemanager.database.dao

import com.waminiyi.realestatemanager.core.database.dao.PointOfInterestDao
import com.waminiyi.realestatemanager.core.database.model.PointOfInterestEntity
import com.waminiyi.realestatemanager.database.TestDataGenerator
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class PointOfInterestDaoTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var poiDao: PointOfInterestDao

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun insertAndGetAllPointOfInterestsTest() = runBlocking {
        // Given: No point of interest added to the database
        var pointOfInterests = poiDao.getAllPointOfInterests()

        // Then: The database should contain no point of interest
        assertEquals(0, pointOfInterests.size)

        // When: Inserting a new point of interest
        val pointOfInterestEntity = TestDataGenerator.getRandomPoi()
        poiDao.insertPointOfInterest(pointOfInterestEntity)

        // Then: The database should contain one point of interest
        pointOfInterests = poiDao.getAllPointOfInterests()
        assertEquals(1, pointOfInterests.size)
        assertEquals(pointOfInterestEntity.pointOfInterestType, pointOfInterests[0].pointOfInterestType)
    }

    @Test
    fun deletePointOfInterestTest() = runBlocking {
        val pointOfInterest1 = TestDataGenerator.getRandomPoi()
        val pointOfInterest2 =TestDataGenerator.getRandomPoi()
        val id1 = poiDao.insertPointOfInterest(pointOfInterest1)
        val id2 = poiDao.insertPointOfInterest(pointOfInterest2)

        // When: deleting the object from the database
        poiDao.deletePointOfInterest(PointOfInterestEntity(id1.toInt(), pointOfInterest1.pointOfInterestType))
        val pointOfInterests = poiDao.getAllPointOfInterests()

        // Then: the object should be successfully deleted
        assertEquals(1, pointOfInterests.size)
        assertEquals(id2.toInt(), pointOfInterests[0].poiId)
    }

    /**
     * Test the getPointOfInterestById function of PointOfInterestDao.
     */
    @Test
    fun getPointOfInterestByIdTest() = runBlocking {
        // Given: multiple PointOfInterestEntity objects in the database
        val pointOfInterest1 = TestDataGenerator.getRandomPoi()
        val pointOfInterest2 = TestDataGenerator.getRandomPoi()
        val id1 = poiDao.insertPointOfInterest(pointOfInterest1)
        val id2 = poiDao.insertPointOfInterest(pointOfInterest2)

        // When: retrieving a point of interest by ID from the database
        val retrievedPointOfInterest = poiDao.getPointOfInterestById(id1.toInt())

        // Then: the retrieved point of interest should match the inserted one
        assertEquals(id1.toInt(), retrievedPointOfInterest?.poiId)
        assertEquals(pointOfInterest1.pointOfInterestType, retrievedPointOfInterest?.pointOfInterestType)
    }
}
