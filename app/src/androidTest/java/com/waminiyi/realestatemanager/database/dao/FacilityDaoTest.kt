package com.waminiyi.realestatemanager.database.dao

import com.waminiyi.realestatemanager.core.database.dao.FacilityDao
import com.waminiyi.realestatemanager.core.database.model.FacilityEntity
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
class FacilityDaoTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var facilityDao: FacilityDao

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    /**
     * Test the insertFacility function of FacilityDao.
     */
    @Test
    fun insertAndGetAllFacilitiesTest() = runBlocking {
        // Given: No facility added to the the database
        var facilities = facilityDao.getAllFacilities()

        // Then: The database should contain no facility
        assertEquals(0, facilities.size)

        // When: Inserting a new facility
        val facilityEntity =TestDataGenerator.getRandomFacility()
        facilityDao.insertFacility(facilityEntity)

        // Then: The database should contain one image
        facilities = facilityDao.getAllFacilities()
        assertEquals(1, facilities.size)
        assertEquals(facilityEntity.type, facilities[0].type)
        assertEquals(facilityEntity.count, facilities[0].count)

    }

    /**
     * Test the deleteFacility function of FacilityDao.
     */
    @Test
    fun deleteFacilityTest() = runBlocking {
        val facility1 = TestDataGenerator.getRandomFacility()
        val facility2 = TestDataGenerator.getRandomFacility()
        val id1 = facilityDao.insertFacility(facility1)
        val id2 = facilityDao.insertFacility(facility2)

        // When: deleting the object from the database
        facilityDao.deleteFacility(FacilityEntity(id1.toInt(), facility1.type, facility1.count))
        val facilities = facilityDao.getAllFacilities()

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
        val facility1 = TestDataGenerator.getRandomFacility()
        val facility2 = TestDataGenerator.getRandomFacility()
        val id1 = facilityDao.insertFacility(facility1)
        val id2 = facilityDao.insertFacility(facility2)

        // When: retrieving all facilities from the database
        val retrievedFacility = facilityDao.getFacilityById(id1.toInt())

        // Then: the retrieved list should contain all inserted facilities
        assertEquals(id1.toInt(), retrievedFacility?.facilityId)
        assertEquals(facility1.type, retrievedFacility?.type)
        assertEquals(facility1.count, retrievedFacility?.count)
    }
}