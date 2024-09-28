package com.waminiyi.realestatemanager.database

import android.database.Cursor
import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry
import com.waminiyi.realestatemanager.Constants
import com.waminiyi.realestatemanager.data.database.RemDatabase
import com.waminiyi.realestatemanager.data.database.dao.AgentDao
import com.waminiyi.realestatemanager.data.database.dao.EstateDao
import com.waminiyi.realestatemanager.data.database.dao.PhotoDao
import com.waminiyi.realestatemanager.data.database.provider.REMContentProvider
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.UUID
import javax.inject.Inject


@HiltAndroidTest
class RemContentProviderTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    private lateinit var provider: REMContentProvider

    @Inject
    lateinit var estateDao: EstateDao

    @Inject
    lateinit var mPhotoDao: PhotoDao

    @Inject
    lateinit var agentDao: AgentDao

    @Inject
    lateinit var database: RemDatabase

    companion object {
        const val baseUri = "content://com.waminiyi.realestatemanager.core.database.provider.REMContentProvider/"
        private val estateUuid1: UUID = UUID.randomUUID()
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
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        hiltRule.inject()
        provider = REMContentProvider()
        provider.setDatabase(database)
        provider.onCreate()
        provider.attachInfo(context, null)

        agentDao.upsertAgent(agent1)
        mPhotoDao.upsertPhoto(image1)
        mPhotoDao.upsertPhoto(image2)
        mPhotoDao.upsertPhoto(image3)
        estateDao.upsertEstate(estate1)
        estateDao.upsertEstate(estate1)
        estateDao.upsertEstate(estate2)
    }


    @Test
    fun getAllEstateWithProvider() {

        val estatesUri =
            Uri.parse("${baseUri}estates")

        val cursor: Cursor? = provider.query(estatesUri, null, null, null, null)

        assertNotNull(cursor)
        cursor?.use {
            assertTrue(it.moveToFirst())
            assertEquals(2, it.count)
        }
    }

    @Test
    fun getSingleEstateWithProvider() {
        val singleEstateUri =
            Uri.parse("${baseUri}estates/${estate1.estateUuid}")

        val cursor: Cursor? = provider.query(
            singleEstateUri,
            null,
            null,
            null,
            null
        )

        assertNotNull(cursor)
        cursor?.use {
            assertTrue(it.moveToFirst())
            assertNotNull(it.getBlob(0))
            assertEquals(1, it.count)
        }
    }


    @Test
    fun getType_estatesDir_ReturnsCorrectMimeType() {
        val uri = Uri.parse("${baseUri}estates")
        val expectedMimeType = "vnd.android.cursor.dir/${REMContentProvider.AUTHORITY}.${Constants.ESTATES_TABLE_NAME}"

        val mimeType = provider.getType(uri)

        assertEquals(expectedMimeType, mimeType)
    }

    @Test
    fun getType_estateItem_ReturnsCorrectMimeType() {
        val estateId = UUID.randomUUID().toString()
        val uri = Uri.parse("${baseUri}estates/$estateId")
        val expectedMimeType = "vnd.android.cursor.item/${REMContentProvider.AUTHORITY}.${Constants.ESTATES_TABLE_NAME}"

        val mimeType = provider.getType(uri)

        assertEquals(expectedMimeType, mimeType)
    }

    @Test
    fun getType_agentsDir_ReturnsCorrectMimeType() {
        val uri = Uri.parse("${baseUri}agents")
        val expectedMimeType = "vnd.android.cursor.dir/${REMContentProvider.AUTHORITY}.${Constants.AGENTS_TABLE_NAME}"

        val mimeType = provider.getType(uri)

        assertEquals(expectedMimeType, mimeType)
    }

    @Test
    fun getType_agentItem_ReturnsCorrectMimeType() {
        val agentId = UUID.randomUUID().toString()
        val uri = Uri.parse("${baseUri}agents/$agentId")
        val expectedMimeType = "vnd.android.cursor.item/${REMContentProvider.AUTHORITY}.${Constants.AGENTS_TABLE_NAME}"

        val mimeType = provider.getType(uri)

        assertEquals(expectedMimeType, mimeType)
    }

    @Test(expected = IllegalArgumentException::class)
    fun getType_unknownUri_throwsIllegalArgumentException() {
        val unknownUri = Uri.parse("${baseUri}unknown")

        provider.getType(unknownUri)
    }

}
