package com.waminiyi.realestatemanager.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.waminiyi.realestatemanager.core.database.dao.*
import com.waminiyi.realestatemanager.core.database.model.*
import com.waminiyi.realestatemanager.core.database.util.Converters

@Database(
    entities = [EstateEntity::class,
        AgentEntity::class,
        PhotoEntity::class,
        LocalChangeEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class RemDatabase : RoomDatabase() {

    abstract fun estateDao(): EstateDao
    abstract fun agentDao(): AgentDao
    abstract fun photoDao(): PhotoDao
    abstract fun localChangeDao(): LocalChangeDao
}