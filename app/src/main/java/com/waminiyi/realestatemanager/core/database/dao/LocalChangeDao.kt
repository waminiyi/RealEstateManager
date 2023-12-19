package com.waminiyi.realestatemanager.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.waminiyi.realestatemanager.core.database.model.LocalChangeEntity
import com.waminiyi.realestatemanager.core.model.data.ClassTag

@Dao
interface LocalChangeDao {
    @Upsert
    suspend fun upsertChange(localChangeEntity: LocalChangeEntity)

    @Query("DELETE FROM local_changes WHERE local_changes.change_uuid = :changeId")
    suspend fun deleteChange(changeId: String)

    @Transaction
    @Query("SELECT * FROM local_changes WHERE class_tag = :classTag")
    suspend fun getChangesByClassTag(classTag: ClassTag): List<LocalChangeEntity>
}