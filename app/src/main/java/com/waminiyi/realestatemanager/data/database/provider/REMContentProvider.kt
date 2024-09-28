package com.waminiyi.realestatemanager.data.database.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.room.Room
import com.waminiyi.realestatemanager.Constants.AGENTS_TABLE_NAME
import com.waminiyi.realestatemanager.Constants.ESTATES_TABLE_NAME
import com.waminiyi.realestatemanager.Constants.PHOTOS_TABLE_NAME
import com.waminiyi.realestatemanager.Constants.REM_DATABASE_NAME
import com.waminiyi.realestatemanager.data.database.RemDatabase
import java.util.UUID


class REMContentProvider : ContentProvider() {

    companion object {
        /** The authority of this content provider.  */
         const val AUTHORITY =
            "com.waminiyi.realestatemanager.core.database.provider.REMContentProvider"

        /**The match code for all items in the estates table.  */
        private const val CODE_REM_ESTATES_DIR = 1

        /** The match code for an item in the estates table.  */
        private const val CODE_REM_ESTATES_ITEM = 2

        /**The match code for all items in the agents table.  */
        private const val CODE_REM_AGENTS_DIR = 3

        /** The match code for an item in the agents table.  */
        private const val CODE_REM_AGENT_ITEM = 4

        /**The match code for all items in the photos table.  */
        private const val CODE_REM_PHOTOS_DIR = 5

        /** The match code for an item in the photos table.  */
        private const val CODE_REM_PHOTO_ITEM = 6

        /** The URI matcher.  */
        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        /**
         * The list of possible codes
         */
        private val codes = listOf(
            CODE_REM_ESTATES_DIR, CODE_REM_ESTATES_ITEM, CODE_REM_AGENTS_DIR,
            CODE_REM_AGENT_ITEM, CODE_REM_PHOTOS_DIR, CODE_REM_PHOTO_ITEM
        )

        init {
            MATCHER.addURI(
                AUTHORITY,
                ESTATES_TABLE_NAME,
                CODE_REM_ESTATES_DIR
            )
            MATCHER.addURI(
                AUTHORITY,
                "$ESTATES_TABLE_NAME/*",
                CODE_REM_ESTATES_ITEM
            )
            MATCHER.addURI(
                AUTHORITY,
                AGENTS_TABLE_NAME,
                CODE_REM_AGENTS_DIR
            )
            MATCHER.addURI(
                AUTHORITY,
                "$AGENTS_TABLE_NAME/*",
                CODE_REM_AGENT_ITEM
            )
            MATCHER.addURI(
                AUTHORITY,
                PHOTOS_TABLE_NAME,
                CODE_REM_PHOTOS_DIR
            )
            MATCHER.addURI(
                AUTHORITY,
                "$PHOTOS_TABLE_NAME/*",
                CODE_REM_PHOTO_ITEM
            )
        }
    }

    private lateinit var database: RemDatabase

    // Setter method for database instance
    fun setDatabase(database: RemDatabase) {
        this.database = database
    }

    override fun onCreate(): Boolean {
        // Default database initialization
        if (!::database.isInitialized) {
            database = Room.databaseBuilder(context!!, RemDatabase::class.java, REM_DATABASE_NAME).build()
        }
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val code = MATCHER.match(uri)

        return if (code in codes) {
            val context = context ?: return null
            val cursor: Cursor? = when (code) {
                CODE_REM_ESTATES_DIR -> database.estateDao().getAllEstatesWithCursor()

                CODE_REM_ESTATES_ITEM -> {
                    uri.lastPathSegment?.let {
                        database.estateDao().getEstateWithDetailsCursorById(UUID.fromString(it))
                    }
                }

                CODE_REM_AGENTS_DIR -> database.agentDao().getAllAgentsWithCursor()
                CODE_REM_AGENT_ITEM -> {
                    uri.lastPathSegment?.let {
                        database.agentDao().getAgentWithCursorById(UUID.fromString(it))
                    }
                }

                CODE_REM_PHOTOS_DIR -> database.photoDao().getAllPhotosWithCursor()
                CODE_REM_PHOTO_ITEM -> {
                    uri.lastPathSegment?.let {
                        database.photoDao().getPhotoWithCursorById(UUID.fromString(it))
                    }
                }

                else -> {
                    require(false) { "Unknown URI: $uri" }
                    null
                }
            }
            cursor?.setNotificationUri(context.contentResolver, uri)
            cursor
        } else {
            require(false) { "Unknown URI: $uri" }
            null
        }
    }

    override fun getType(uri: Uri): String {
        return when (MATCHER.match(uri)) {
            CODE_REM_ESTATES_DIR -> "vnd.android.cursor.dir/$AUTHORITY.$ESTATES_TABLE_NAME"
            CODE_REM_ESTATES_ITEM -> "vnd.android.cursor.item/$AUTHORITY.$ESTATES_TABLE_NAME"
            CODE_REM_AGENTS_DIR -> "vnd.android.cursor.dir/$AUTHORITY.$AGENTS_TABLE_NAME"
            CODE_REM_AGENT_ITEM -> "vnd.android.cursor.item/$AUTHORITY.$AGENTS_TABLE_NAME"
            CODE_REM_PHOTOS_DIR -> "vnd.android.cursor.dir/$AUTHORITY.$PHOTOS_TABLE_NAME"
            CODE_REM_PHOTO_ITEM -> "vnd.android.cursor.item/$AUTHORITY.$PHOTOS_TABLE_NAME"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }
}
