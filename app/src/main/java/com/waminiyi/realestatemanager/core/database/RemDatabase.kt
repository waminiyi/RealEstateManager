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
        LocalChangeEntity::class,
        FacilityEntity::class,
        PointOfInterestEntity::class,
        EstateAndFacilityLink::class,
        EstateAndPoiLink::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class RemDatabase : RoomDatabase() {

    abstract fun estateDao(): EstateDao
    abstract fun agentDao(): AgentDao
    abstract fun imageDao(): PhotoDao
    abstract fun facilityDao(): FacilityDao
    abstract fun pointOfInterestDao(): PointOfInterestDao
    abstract fun estateAndFacilityLinkDao(): EstateAndFacilityLinkDao
    abstract fun estateAndPoiLinkDao(): EstateAndPoiLinkDao
    abstract fun localChangeDao(): LocalChangeDao
}