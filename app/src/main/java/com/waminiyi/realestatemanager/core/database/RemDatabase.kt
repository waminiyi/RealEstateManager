package com.waminiyi.realestatemanager.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.waminiyi.realestatemanager.core.database.dao.*
import com.waminiyi.realestatemanager.core.database.model.*
import com.waminiyi.realestatemanager.core.database.util.DateConverter

@Database(
    entities = [EstateEntity::class,
        AgentEntity::class,
        ImageEntity::class,
        FacilityEntity::class,
        PointOfInterestEntity::class,
        EstateAndFacilityLink::class,
        EstateAndPoiLink::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class RemDatabase : RoomDatabase() {

    abstract fun estateDao(): EstateDao
    abstract fun agentDao(): AgentDao
    abstract fun imageDao(): ImageDao
    abstract fun facilityDao(): FacilityDao
    abstract fun pointOfInterestDao(): PointOfInterestDao
    abstract fun estateAndFacilityLinkDao(): EstateAndFacilityLinkDao
    abstract fun estateAndPoiLinkDao(): EstateAndPoiLinkDao
}