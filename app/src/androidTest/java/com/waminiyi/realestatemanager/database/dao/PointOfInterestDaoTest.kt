package com.waminiyi.realestatemanager.database.dao

import android.content.Context
import androidx.room.*
import androidx.test.core.app.ApplicationProvider
import com.waminiyi.realestatemanager.core.database.RemDatabase
import com.waminiyi.realestatemanager.core.database.dao.PointOfInterestDao
import com.waminiyi.realestatemanager.core.database.model.PointOfInterestEntity
import com.waminiyi.realestatemanager.core.model.data.PointOfInterestType
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
class PointOfInterestDaoTest {
    private lateinit var database: RemDatabase
    private lateinit var poiDao: PointOfInterestDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, RemDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        poiDao = database.pointOfInterestDao()
    }

    @After
    fun tearDown() {
        database.clearAllTables()
        database.close()
    }

    @Test
    fun insertAndGetAllPointOfInterestsTest() = runBlocking {
        // Given: No point of interest added to the database
        var pointOfInterests = poiDao.getAllPointOfInterests().first()

        // Then: The database should contain no point of interest
        assertEquals(0, pointOfInterests.size)

        // When: Inserting a new point of interest
        val pointOfInterestEntity = PointOfInterestEntity(pointOfInterestType = PointOfInterestType.RESTAURANT)
        poiDao.insertPointOfInterest(pointOfInterestEntity)

        // Then: The database should contain one point of interest
        pointOfInterests = poiDao.getAllPointOfInterests().first()
        assertEquals(1, pointOfInterests.size)
        assertEquals(pointOfInterestEntity.pointOfInterestType, pointOfInterests[0].pointOfInterestType)
    }

    @Test
    fun deletePointOfInterestTest() = runBlocking {
        val pointOfInterest1 = PointOfInterestEntity(pointOfInterestType = PointOfInterestType.RESTAURANT)
        val pointOfInterest2 = PointOfInterestEntity(pointOfInterestType = PointOfInterestType.PARK)
        val id1 = poiDao.insertPointOfInterest(pointOfInterest1)
        val id2 = poiDao.insertPointOfInterest(pointOfInterest2)

        // When: deleting the object from the database
        poiDao.deletePointOfInterest(PointOfInterestEntity(id1.toInt(), pointOfInterest1.pointOfInterestType))
        val pointOfInterests = poiDao.getAllPointOfInterests().first()

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
        val pointOfInterest1 = PointOfInterestEntity(pointOfInterestType = PointOfInterestType.RESTAURANT)
        val pointOfInterest2 = PointOfInterestEntity(pointOfInterestType = PointOfInterestType.PARK)
        val id1 = poiDao.insertPointOfInterest(pointOfInterest1)
        val id2 = poiDao.insertPointOfInterest(pointOfInterest2)

        // When: retrieving a point of interest by ID from the database
        val retrievedPointOfInterest = poiDao.getPointOfInterestById(id1.toInt()).first()

        // Then: the retrieved point of interest should match the inserted one
        assertEquals(id1.toInt(), retrievedPointOfInterest?.poiId)
        assertEquals(pointOfInterest1.pointOfInterestType, retrievedPointOfInterest?.pointOfInterestType)
    }
}
