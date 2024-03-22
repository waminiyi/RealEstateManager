package com.waminiyi.realestatemanager.database

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.nio.ByteBuffer
import java.util.UUID


@HiltAndroidTest
class RemContentProviderTest {
    // FOR DATA

    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    private var mContentResolver: ContentResolver? = null

    @Before
    fun setUp() = runBlocking {
        hiltRule.inject()
        mContentResolver = InstrumentationRegistry.getInstrumentation().context.contentResolver;
    }


    @Test
    fun getAllEstateWithProvider() {

        val estatesUri =
            Uri.parse("content://com.waminiyi.realestatemanager.core.database.provider.REMContentProvider/estates")

        val cursor: Cursor? = mContentResolver!!.query(estatesUri, null, null, null, null)

        assertNotNull(cursor)
        cursor?.use { assertTrue(it.moveToFirst()) }
    }

    @Test
    fun getSingleEstateWithProvider() {
        val estateUuid = "78d8370c-2011-4d56-ad10-22fe59522b1e"

        val singleEstateUri =
            Uri.parse("content://com.waminiyi.realestatemanager.core.database.provider.REMContentProvider/estates/$estateUuid")

        val cursor: Cursor? = mContentResolver!!.query(
            singleEstateUri,
            null,
            null,
            arrayOf(estateUuid),
            null
        )

        assertNotNull(cursor)
        cursor?.use {
            assertTrue(it.moveToFirst())
            assertNotNull(it.getBlob(0))
        }
    }

    private fun convertByteArrayToUUID(uuidByteArray: ByteArray): UUID {
        return ByteBuffer.wrap(uuidByteArray).let {
            val mostSignificantBits = it.long
            val leastSignificantBits = it.long
            UUID(mostSignificantBits, leastSignificantBits)
        }
    }
}