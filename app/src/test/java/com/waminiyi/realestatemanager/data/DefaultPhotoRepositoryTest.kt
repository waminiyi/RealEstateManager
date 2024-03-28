package com.waminiyi.realestatemanager.data

import com.waminiyi.realestatemanager.core.data.repository.DefaultPhotoRepository
import com.waminiyi.realestatemanager.core.database.dao.PhotoDao
import com.waminiyi.realestatemanager.core.database.model.PhotoEntity
import com.waminiyi.realestatemanager.core.model.data.DataResult
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okio.IOException
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.UUID

class DefaultPhotoRepositoryTest {

    private lateinit var photoDao: PhotoDao
    private lateinit var repository: DefaultPhotoRepository
    private lateinit var photoEntity: PhotoEntity
    val photoUuid = UUID.randomUUID()
    val estateUuid = UUID.randomUUID()

    @Before
    fun setUp() {
        photoDao = mockk()
        repository = DefaultPhotoRepository(photoDao)
        photoEntity = PhotoEntity(
            photoUuid = photoUuid,
            estateUuid = estateUuid,
            url = "https://example.com/photo.jpg",
            localPath = "/path/to/photo.jpg",
            isMainPhoto = true,
            description = "A beautiful view"
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `savePhoto in repository should call upsertPhoto in PhotoDao successfully`() = runBlocking {
        // Given
        coEvery { photoDao.upsertPhoto(any()) } returns Unit

        // When
        val result = repository.savePhoto(photoEntity.asPhoto())

        // Then
        assert(result is DataResult.Success)
        coVerify(exactly = 1) { photoDao.upsertPhoto(photoEntity) }
    }

    @Test
    @Throws(IOException::class)
    fun `savePhoto should return error when exception occurs`() = runBlocking {
        // Given
        coEvery { photoDao.upsertPhoto(any()) } throws IOException()

        // When
        val result = repository.savePhoto(photoEntity.asPhoto())

        // Then
        assert(result is DataResult.Error)
        coVerify(exactly = 1) { photoDao.upsertPhoto(photoEntity) }
    }

    @Test
    fun `getPhotosByEstate in PhotoRepository should call getPhotosByEstate in PhotoDao`() = runBlocking {
        // Given
        val photoEntities = listOf(photoEntity)
        coEvery { photoDao.getPhotosByEstate(estateUuid) } returns photoEntities

        // When
        val result = repository.getAllEstatePhotos(estateUuid.toString())

        // Then
        assert(result is DataResult.Success)
        assert((result as DataResult.Success).data.size == photoEntities.size)
        coVerify(exactly = 1) { photoDao.getPhotosByEstate(estateUuid) }
    }

    @Test
    @Throws(IOException::class)
    fun `getPhotosByEstate should return error when exception occurs`() = runBlocking {
        // Given
        coEvery { photoDao.getPhotosByEstate(any()) } throws IOException()

        // When
        val result = repository.getAllEstatePhotos(estateUuid.toString())

        // Then
        assert(result is DataResult.Error)
        coVerify(exactly = 1) { photoDao.getPhotosByEstate(estateUuid) }
    }

    @Test
    fun `getPhoto in PhotoRepository should call getPhotoById in PhotoDao`() = runBlocking {
        // Given
        coEvery { photoDao.getPhotoById(any()) } returns photoEntity

        // When
        val result = repository.getPhoto(estateUuid.toString())

        // Then
        assertEquals(DataResult.Success(photoEntity.asPhoto()), result)
        coVerify(exactly = 1) { photoDao.getPhotoById(estateUuid) }
    }
}