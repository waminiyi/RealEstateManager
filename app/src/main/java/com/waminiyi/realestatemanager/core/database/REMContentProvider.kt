package com.waminiyi.realestatemanager.core.database

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.room.Room
import com.waminiyi.realestatemanager.core.Constants.ESTATES_TABLE_NAME
import com.waminiyi.realestatemanager.core.Constants.REM_DATABASE_NAME


class REMContentProvider : ContentProvider() {

    companion object {
        /** The authority of this content provider.  */
        const val AUTHORITY = "com.waminiyi.realestatemanager.REMContentProvider"

        /** The URI for the estates table.  */
        val URI_MENU = Uri.parse(
            "content://$AUTHORITY/$ESTATES_TABLE_NAME"
        )

        /**The match code for some items in the estates table.  */
        private const val CODE_REM_ESTATES_DIR = 1

        /** The match code for an item in the estates table.  */
        private const val CODE_REM_ESTATES_ITEM = 2

        /** The URI matcher.  */
        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

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
        }
    }

    private lateinit var database: RemDatabase

    override fun onCreate(): Boolean {
        database =
            Room.databaseBuilder(context!!, RemDatabase::class.java, REM_DATABASE_NAME).build()
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
        return if (code == CODE_REM_ESTATES_DIR || code == CODE_REM_ESTATES_ITEM) {
            val context = context ?: return null
            val cursor: Cursor? = if (code == CODE_REM_ESTATES_DIR) {

                database.estateDao().getAllEstatesWithDetailsCursor()
            } else {
                val estateUuid = uri.lastPathSegment // Assuming the UUID is part of the URI
                estateUuid?.let { database.estateDao().getEstateWithDetailsCursorById(it) }!!
            }
            cursor?.setNotificationUri(context.contentResolver, uri)
            cursor
        } else {
            throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String? {
        return null
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
