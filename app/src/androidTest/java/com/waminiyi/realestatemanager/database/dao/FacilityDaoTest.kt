package com.waminiyi.realestatemanager.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.waminiyi.realestatemanager.core.database.RemDatabase
import com.waminiyi.realestatemanager.core.database.dao.FacilityDao
import com.waminiyi.realestatemanager.core.database.model.FacilityEntity
import com.waminiyi.realestatemanager.core.model.data.FacilityType
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
class FacilityDaoTest {
    private lateinit var database: RemDatabase
    private lateinit var facilityDao: FacilityDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, RemDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        facilityDao = database.facilityDao()
    }

    @After
    fun tearDown() {
        database.clearAllTables()
        database.close()
    }

    /**
     * Test the insertFacility function of FacilityDao.
     */
    @Test
    fun insertAndGetAllFacilitiesTest() = runBlocking {
        // Given: No facility added to the the database
        var facilities = facilityDao.getAllFacilities().first()

        // Then: The database should contain no facility
        assertEquals(0, facilities.size)

        // When: Inserting a new facility
        val facilityEntity = FacilityEntity(type = FacilityType.LIVING_ROOM, count = 1)
        facilityDao.insertFacility(facilityEntity)

        // Then: The database should contain one image
        facilities = facilityDao.getAllFacilities().first()
        assertEquals(1, facilities.size)
        assertEquals(facilityEntity.type, facilities[0].type)
        assertEquals(facilityEntity.count, facilities[0].count)

    }

    /**
     * Test the deleteFacility function of FacilityDao.
     */
    @Test
    fun deleteFacilityTest() = runBlocking {
        val facility1 = FacilityEntity(type = FacilityType.LIVING_ROOM, count = 1)
        val facility2 = FacilityEntity(type = FacilityType.BEDROOM, count = 3)
        val id1 = facilityDao.insertFacility(facility1)
        val id2 = facilityDao.insertFacility(facility2)

        // When: deleting the object from the database
        facilityDao.deleteFacility(FacilityEntity(id1.toInt(), facility1.type, facility1.count))
        val facilities = facilityDao.getAllFacilities().first()

        // Then: the object should be successfully deleted
        assertEquals(1, facilities.size)
        assertEquals(id2.toInt(), facilities[0].facilityId)

    }

    /**
     * Test the getAllFacilities function of FacilityDao.
     */
    @Test
    fun getFacilityByIdTest() = runBlocking {
        // Given: multiple FacilityEntity objects in the database
        val facility1 = FacilityEntity(type = FacilityType.LIVING_ROOM, count = 1)
        val facility2 = FacilityEntity(type = FacilityType.BEDROOM, count = 3)
        val id1 = facilityDao.insertFacility(facility1)
        val id2 = facilityDao.insertFacility(facility2)

        // When: retrieving all facilities from the database
        val retrievedFacility = facilityDao.getFacilityById(id1.toInt()).first()

        // Then: the retrieved list should contain all inserted facilities
        assertEquals(id1.toInt(), retrievedFacility?.facilityId)
        assertEquals(facility1.type, retrievedFacility?.type)
        assertEquals(facility1.count, retrievedFacility?.count)
    }
}