package com.waminiyi.realestatemanager.database.dao

import com.waminiyi.realestatemanager.data.database.dao.PhotoDao
import com.waminiyi.realestatemanager.database.TestDataGenerator
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import javax.inject.Inject

@HiltAndroidTest
class PhotoDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var mPhotoDao: PhotoDao

    companion object {
        val ownerUuid1: UUID = UUID.randomUUID()
        val imageEntity1 = TestDataGenerator.getRandomImage(ownerUuid1, true)
        val imageEntity2 = TestDataGenerator.getRandomImage(ownerUuid1, false)
        val imageEntity3 = TestDataGenerator.getRandomImage(UUID.randomUUID(), true)
    }

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun upsertImageTest() = runBlocking {
        // Given: No images added to the the database
        var allImages = mPhotoDao.getAllPhotos()

        // Then: The database should contain no image
        assertEquals(0, allImages.size)

        // When: Inserting a new image
        mPhotoDao.upsertPhoto(imageEntity1)

        // Then: The database should contain one image
        allImages = mPhotoDao.getAllPhotos()
        assertEquals(1, allImages.size)
        assertEquals(imageEntity1, allImages[0])

        // When: Updating the existing image's description
        val updatedImage = imageEntity1.copy(description = "Updated description")
        mPhotoDao.upsertPhoto(updatedImage)

        // Then: The database should still contain only one image with the updated description
        allImages = mPhotoDao.getAllPhotos()
        assertEquals(1, allImages.size)
        assertEquals(imageEntity1.photoUuid, allImages[0].photoUuid)
        assertNotEquals(imageEntity1.description, allImages[0].description)

    }

    @Test
    fun deleteImage() = runBlocking {
        // Given: Two images in the database
        mPhotoDao.upsertPhoto(imageEntity1)
        mPhotoDao.upsertPhoto(imageEntity2)

        // When: Deleting one image
        mPhotoDao.deletePhoto(imageEntity1)

        // Then: The database should contain only the remaining image
        val allImages = mPhotoDao.getAllPhotos()
        assertEquals(1, allImages.size)
        assertTrue("The list doesn't contain this image", allImages.contains(imageEntity2))
        assertFalse("The list contains this image", allImages.contains(imageEntity1))
    }

    @Test
    fun getImagesByOwner() = runBlocking {


        mPhotoDao.upsertPhoto(imageEntity1)
        mPhotoDao.upsertPhoto(imageEntity2)
        mPhotoDao.upsertPhoto(imageEntity3)

        val imagesByOwner = mPhotoDao.getPhotosByEstate(ownerUuid1)
        assertEquals(2, imagesByOwner.size)
        assertTrue(imagesByOwner.contains(imageEntity1))
        assertTrue(imagesByOwner.contains(imageEntity2))
    }
}
