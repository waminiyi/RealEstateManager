package com.waminiyi.realestatemanager.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.waminiyi.realestatemanager.data.database.dao.AgentDao
import com.waminiyi.realestatemanager.data.database.dao.EstateDao
import com.waminiyi.realestatemanager.data.database.dao.PhotoDao
import com.waminiyi.realestatemanager.data.database.model.AgentEntity
import com.waminiyi.realestatemanager.data.database.model.EstateEntity
import com.waminiyi.realestatemanager.data.database.model.PhotoEntity
import com.waminiyi.realestatemanager.data.database.util.Converters

@Database(
    entities = [EstateEntity::class,
        AgentEntity::class,
        PhotoEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class RemDatabase : RoomDatabase() {

    abstract fun estateDao(): EstateDao
    abstract fun agentDao(): AgentDao
    abstract fun photoDao(): PhotoDao
}