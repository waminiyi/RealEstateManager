package com.waminiyi.realestatemanager.data

import com.waminiyi.realestatemanager.data.repository.DefaultEstateRepository
import com.waminiyi.realestatemanager.data.repository.FilterRepository
import com.waminiyi.realestatemanager.data.database.dao.EstateDao
import com.waminiyi.realestatemanager.data.database.model.AddressEntity
import com.waminiyi.realestatemanager.data.database.model.EstateEntity
import com.waminiyi.realestatemanager.data.database.model.EstateWithDetailsEntity
import com.waminiyi.realestatemanager.data.database.model.PhotoEntity
import com.waminiyi.realestatemanager.data.models.Result
import com.waminiyi.realestatemanager.data.models.EstateStatus
import com.waminiyi.realestatemanager.data.models.EstateType
import com.waminiyi.realestatemanager.data.models.Filter
import com.waminiyi.realestatemanager.data.models.Location
import com.waminiyi.realestatemanager.data.models.PointOfInterest
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Date
import java.util.UUID

class DefaultEstateRepositoryTest {


    private lateinit var estateRepository: DefaultEstateRepository
    private lateinit var estateDao: EstateDao
    private lateinit var filterRepository: FilterRepository
    private lateinit var estateEntity: EstateEntity
    private lateinit var photoEntity: PhotoEntity

    @Before
    fun setUp() {
        estateDao = mockk()
        filterRepository = mockk()
        estateRepository = DefaultEstateRepository(estateDao, filterRepository)
        val daoQueryResult: Map.Entry<EstateEntity, List<PhotoEntity>> = mockk()
        estateEntity = EstateEntity(
            estateUuid = UUID.randomUUID(),
            type = EstateType.APARTMENT,
            price = 100000,
            area = 150,
            roomsCount = 3,
            bedroomsCount = 2,
            bathroomsCount = 2,
            description = "A beautiful apartment",
            addressEntity = AddressEntity(
                streetNumber = 123,
                streetName = "Main Street",
                city = "New York",
                state = "NY",
                country = "USA",
                postalCode = 10001,
                location = Location(latitude = 40.7128, longitude = -74.0060)
            ),
            estateStatus = EstateStatus.AVAILABLE,
            entryDate = Date(),
            saleDate = null,
            agentId = UUID.randomUUID(),
            poiList = listOf(PointOfInterest.PARK, PointOfInterest.SCHOOL)
        )

        photoEntity = PhotoEntity(
            photoUuid = UUID.randomUUID(),
            estateUuid = estateEntity.estateUuid,
            url = "https://example.com/photo.jpg",
            localPath = "/path/to/photo.jpg",
            isMainPhoto = true,
            description = "A beautiful view"
        )

        coEvery { filterRepository.filter } returns flowOf(Filter())
        every { daoQueryResult.key } returns estateEntity
        every { daoQueryResult.value } returns listOf(photoEntity)
        coEvery { estateDao.getAllEstatesWithImages(any()) } returns flowOf(mapOf((daoQueryResult.key to daoQueryResult.value)))
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getAllEstatesStream should return DataResult Success with list of estates`() = runBlocking {

        estateRepository.getAllEstatesStream().take(1).collect {
            assert(it is com.waminiyi.realestatemanager.data.models.DataResult.Result.Success)
            coVerify(exactly = 1) { filterRepository.filter }
            coVerify(exactly = 1) { estateDao.getAllEstatesWithImages(any()) }
            assertEquals(listOf(estateEntity.asEstate(photoEntity.asPhoto())), (it as com.waminiyi.realestatemanager.data.models.DataResult.Result.Success).data)
        }
    }

    @Test
    fun `getEstateWithDetails should return DataResult Success with estate`() = runBlocking {
        val estateWithDetailsEntity: EstateWithDetailsEntity = mockk()
        every { estateWithDetailsEntity.asEstateWithDetails() } returns mockk()
        coEvery { estateDao.getEstateWithDetailsById(any()) } returns estateWithDetailsEntity

        val result = estateRepository.getEstateWithDetails(estateEntity.estateUuid.toString())

        assert(result is com.waminiyi.realestatemanager.data.models.DataResult.Result.Success)
        coVerify(exactly = 1) { estateDao.getEstateWithDetailsById(estateEntity.estateUuid) }
        assertEquals(estateWithDetailsEntity.asEstateWithDetails(), (result as com.waminiyi.realestatemanager.data.models.DataResult.Result.Success).data)

    }
}