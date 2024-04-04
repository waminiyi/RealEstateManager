package com.waminiyi.realestatemanager.database.dao

import com.waminiyi.realestatemanager.core.database.dao.AgentDao
import com.waminiyi.realestatemanager.core.database.dao.EstateDao
import com.waminiyi.realestatemanager.core.database.dao.PhotoDao
import com.waminiyi.realestatemanager.core.model.EstateStatus
import com.waminiyi.realestatemanager.core.model.EstateType
import com.waminiyi.realestatemanager.database.TestDataGenerator
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.UUID
import javax.inject.Inject

@HiltAndroidTest
class EstateDaoTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var estateDao: EstateDao

    @Inject
    lateinit var mPhotoDao: PhotoDao

    @Inject
    lateinit var agentDao: AgentDao

    companion object {
        val estateUuid1: UUID = UUID.randomUUID()
        private val estateUuid2: UUID = UUID.randomUUID()
        val image1 = TestDataGenerator.getRandomImage(estateUuid1, true)
        val image2 = TestDataGenerator.getRandomImage(estateUuid2, true)
        val image3 = TestDataGenerator.getRandomImage(estateUuid1, false)
        val agent1 = TestDataGenerator.getRandomAgent()
        val estate1 = TestDataGenerator.getRandomEstate(estateUuid1, agent1.agentUuid)
        val estate2 = TestDataGenerator.getRandomEstate(estateUuid2, agent1.agentUuid)
    }

    @Before
    fun setUp() = runBlocking {
        hiltRule.inject()
        agentDao.upsertAgent(agent1)
        mPhotoDao.upsertPhoto(image1)
        mPhotoDao.upsertPhoto(image2)
        mPhotoDao.upsertPhoto(image3)
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
        assertEquals(image1, estatesWithImages[estate1]!!.first())

        // When: Inserting another estate
        estateDao.upsertEstate(estate2)

        // Then: The database should contain two estates
        estatesWithImages = estateDao.getAllEstatesWithImages().first()
        assertEquals(2, estatesWithImages.size)
        assertEquals(image1, estatesWithImages[estate1]!!.first())
        assertEquals(image2, estatesWithImages[estate2]!!.first())

        // When: Updating existing estate
        val updatedEstate = estate1.copy(estateStatus = EstateStatus.SOLD)
        estateDao.upsertEstate(updatedEstate)

        // Then: The database should contain two estates and the target estate should be up to date
        estatesWithImages = estateDao.getAllEstatesWithImages().first()
        assertEquals(2, estatesWithImages.size)
        assertEquals(image1, estatesWithImages[updatedEstate]!!.first())
//        assertEquals(Status.SOLD, estatesWithImages[0].estateEntity.status)
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

    @Test
    fun filterEstatesByPriceTest() = runBlocking {
        // Given: Estates with different prices in the database
        val estateLowPrice = estate1.copy(price = 100000)
        val estateHighPrice = estate2.copy(price = 500000)
        estateDao.upsertEstate(estateLowPrice)
        estateDao.upsertEstate(estateHighPrice)

        // When: Filtering estates by price range
        val filteredEstates = estateDao.getAllEstatesWithImages(minPrice = 200000, maxPrice = 400000).first()

        // Then: Only estates within the specified price range should be returned
        assertEquals(0, filteredEstates.size)

        // When: Filtering estates by another price range
        val newFilteredEstates = estateDao.getAllEstatesWithImages(minPrice = 80000, maxPrice = 150000).first()

        // Then: Only estates within the new specified price range should be returned
        assertEquals(1, newFilteredEstates.size)
        assertTrue(newFilteredEstates.containsKey(estateLowPrice))
    }

    @Test
    fun filterEstatesByAreaTest() = runBlocking {
        // Given: Estates with different areas in the database
        val estateSmallArea = estate1.copy(area = 50)
        val estateLargeArea = estate2.copy(area = 200)
        estateDao.upsertEstate(estateSmallArea)
        estateDao.upsertEstate(estateLargeArea)

        // When: Filtering estates by area range
        val filteredEstates = estateDao.getAllEstatesWithImages(minArea = 100, maxArea = 150).first()

        // Then: Only estates within the specified area range should be returned
        assertEquals(0, filteredEstates.size)

        // When: Filtering estates by another area range
        val newFilteredEstates = estateDao.getAllEstatesWithImages(minArea = 30, maxArea = 100).first()

        // Then: Only estates within the new specified area range should be returned
        assertEquals(1, newFilteredEstates.size)
        assertTrue(newFilteredEstates.containsKey(estateSmallArea))
    }

    @Test
    fun filterEstatesByTypeTest() = runBlocking {
        // Given: Estates with different types in the database
        val estateType1 = estate1.copy(type = EstateType.HOUSE)
        val estateType2 = estate2.copy(type = EstateType.APARTMENT)
        estateDao.upsertEstate(estateType1)
        estateDao.upsertEstate(estateType2)

        // When: Filtering estates by type
        val filteredEstates = estateDao.getAllEstatesWithImages(
            typesIsEmpty = false, types = listOf(EstateType.APARTMENT)
        ).first()

        // Then: Only estates of the specified type should be returned
        assertEquals(1, filteredEstates.size)
        assertTrue(filteredEstates.containsKey(estateType2))
    }

    @Test
    fun filterEstatesByCityTest() = runBlocking {
        // Given: Estates with different cities in the database
        val estateCity1 = estate1.copy(addressEntity = estate1.addressEntity.copy(city = "New York"))
        val estateCity2 = estate2.copy(addressEntity = estate2.addressEntity.copy(city = "Los Angeles"))
        estateDao.upsertEstate(estateCity1)
        estateDao.upsertEstate(estateCity2)

        // When: Filtering estates by city
        val filteredEstates = estateDao.getAllEstatesWithImages(
            citiesIsEmpty = false, cities = listOf("Los Angeles")
        ).first()

        // Then: Only estates located in the specified city should be returned
        assertEquals(1, filteredEstates.size)
        assertTrue(filteredEstates.containsKey(estateCity2))
    }
}
