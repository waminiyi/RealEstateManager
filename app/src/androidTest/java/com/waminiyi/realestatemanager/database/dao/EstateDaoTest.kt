package com.waminiyi.realestatemanager.database.dao

import android.content.Context
import androidx.room.*
import androidx.test.core.app.ApplicationProvider
import com.waminiyi.realestatemanager.core.database.RemDatabase
import com.waminiyi.realestatemanager.core.database.dao.AgentDao
import com.waminiyi.realestatemanager.core.database.dao.EstateDao
import com.waminiyi.realestatemanager.core.database.dao.ImageDao
import com.waminiyi.realestatemanager.core.database.model.*
import com.waminiyi.realestatemanager.core.model.data.EstateType
import com.waminiyi.realestatemanager.core.model.data.ImageType
import com.waminiyi.realestatemanager.core.model.data.Status
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import java.util.*

@HiltAndroidTest
class EstateDaoTest {
    private lateinit var database: RemDatabase
    private lateinit var estateDao: EstateDao
    private lateinit var imageDao: ImageDao
    private lateinit var agentDao: AgentDao

    companion object {
        val estateUuid1: UUID = UUID.randomUUID()
        val estateUuid2: UUID = UUID.randomUUID()
        val mainImageUuid1: UUID = UUID.randomUUID()
        val image1 = ImageEntity(
            mainImageUuid1, estateUuid1,
            "image_name1", "description1", ImageType.MAIN
        )
        val image2 = ImageEntity(
            UUID.randomUUID(), estateUuid2,
            "image_name2", "description2", ImageType.MAIN
        )
        val image3 = ImageEntity(
            UUID.randomUUID(), estateUuid1,
            "image_name3", "description3", ImageType.ADDITIONAL
        )
        val agent1 = AgentEntity(UUID.randomUUID(), "John", "Doe", "agent@mail.com", "123-456")
        val estate1 = EstateEntity(
            estateUuid = estateUuid1,
            type = EstateType.APARTMENT,
            price = 1500000,
            area = 150.0f,
            description = "Top Apartment",
            address = AddressEntity(
                123,
                "Main St",
                "Gre",
                "Isère",
                38600,
                LocationEntity(
                    3205211.0,
                    325000.0
                )
            ),
            status = Status.AVAILABLE,
            entryDate = Date(),
            mainImageId = mainImageUuid1,
            agentId = agent1.agentUuid
        )

        val estate2 = EstateEntity(
            estateUuid = estateUuid2,
            type = EstateType.HOUSE,
            price = 8000000,
            area = 300.0f,
            description = "Beautiful house",
            address = AddressEntity(
                123,
                "Rose St",
                "Esch",
                "Isère",
                38100,
                LocationEntity(
                    3515211.0,
                    225000.0
                )
            ),
            status = Status.AVAILABLE,
            entryDate = Date(),
            mainImageId = image2.imageUuid,
            agentId = agent1.agentUuid
        )
    }

    @Before
    fun setUp() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, RemDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        estateDao = database.estateDao()
        imageDao = database.imageDao()
        agentDao=database.agentDao()
        agentDao.upsertAgent(agent1)
        imageDao.upsertImage(image1)
        imageDao.upsertImage(image2)
        imageDao.upsertImage(image3)
    }

    @After
    fun tearDown() {
        database.clearAllTables()
        database.close()
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
        val updatedEstate= estate1.copy(status = Status.SOLD)
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
        val retrievedEstate = estateDao.getEstateWithDetailsById(estateUuid1).first()

        // Then: the retrieved estate should match the estate with the passed id
        assertNotNull(retrievedEstate)
        assertEquals(estate1, retrievedEstate?.estateEntity)
    }
}
