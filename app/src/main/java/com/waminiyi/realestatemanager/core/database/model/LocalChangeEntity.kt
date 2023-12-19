package com.waminiyi.realestatemanager.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.waminiyi.realestatemanager.core.model.data.ClassTag

/**
 * Local representation of a change list for a model.
 *
 * Change lists are a representation of a server-side map like data structure of model ids to
 * metadata about that model. In a single change list, a given model id can only show up once.
 */
@Entity(tableName = "local_changes")
data class LocalChangeEntity(
    /**
     * The id of the object that was changed
     */
    @PrimaryKey
    @ColumnInfo(name = "change_uuid")
    val id: String,

    /**
     * The unique tag of the object class name
     */
    @ColumnInfo(name = "class_tag")
    val classTag: ClassTag,

    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean
)