package com.waminiyi.realestatemanager.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.waminiyi.realestatemanager.core.database.RemDatabase
import com.waminiyi.realestatemanager.core.database.dao.ImageDao
import com.waminiyi.realestatemanager.core.database.model.ImageEntity
import com.waminiyi.realestatemanager.core.model.data.ImageType
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*

@HiltAndroidTest
class ImageDaoTest {

    private lateinit var database: RemDatabase
    private lateinit var imageDao: ImageDao

    companion object {
        val ownerUuid1: UUID = UUID.randomUUID()
        val imageEntity1 = ImageEntity(
            UUID.randomUUID(), ownerUuid1,
            "image_name1", "description1", ImageType.MAIN
        )
        val imageEntity2 = ImageEntity(
            UUID.randomUUID(), ownerUuid1,
            "image_name2", "description2", ImageType.ADDITIONAL
        )
        val imageEntity3 = ImageEntity(
            UUID.randomUUID(), UUID.randomUUID(),
            "image_name3", "description3", ImageType.MAIN
        )
    }

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, RemDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        imageDao = database.imageDao()
    }

    @After
    fun tearDown() {
        database.clearAllTables()
        database.close()
    }

    /**
     * Test the upsertImage function of ImageDao.
     */
    @Test
    fun upsertImageTest() = runBlocking {
        // Given: No images added to the the database
        var allImages = imageDao.getAllImages().first()

        // Then: The database should contain no image
        assertEquals(0, allImages.size)

        // When: Inserting a new image
        imageDao.upsertImage(imageEntity1)

        // Then: The database should contain one image
        allImages = imageDao.getAllImages().first()
        assertEquals(1, allImages.size)
        assertEquals(imageEntity1, allImages[0])

        // When: Updating the existing image's description
        val updatedImage = imageEntity1.copy(description = "Updated description")
        imageDao.upsertImage(updatedImage)

        // Then: The database should still contain only one image with the updated description
        allImages = imageDao.getAllImages().first()
        assertEquals(1, allImages.size)
        assertEquals(imageEntity1.imageUuid, allImages[0].imageUuid)
        assertNotEquals(imageEntity1.description, allImages[0].description)

    }

    @Test
    fun deleteImage() = runBlocking {
        // Given: Two images in the database
        imageDao.upsertImage(imageEntity1)
        imageDao.upsertImage(imageEntity2)

        // When: Deleting one image
        imageDao.deleteImage(imageEntity1)

        // Then: The database should contain only the remaining image
        val allImages = imageDao.getAllImages().first()
        assertEquals(1, allImages.size)
        assertTrue("The list doesn't contain this image", allImages.contains(imageEntity2))
        assertFalse("The list contains this image", allImages.contains(imageEntity1))
    }

    @Test
    fun getImagesByOwner() = runBlocking {


        imageDao.upsertImage(imageEntity1)
        imageDao.upsertImage(imageEntity2)
        imageDao.upsertImage(imageEntity3)

        val imagesByOwner = imageDao.getImagesByOwner(ownerUuid1).first()
        assertEquals(2, imagesByOwner.size)
        assertTrue(imagesByOwner.contains(imageEntity1))
        assertTrue(imagesByOwner.contains(imageEntity2))
    }
}
