package com.waminiyi.realestatemanager.core.database.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.waminiyi.realestatemanager.core.database.RemDatabase
import com.waminiyi.realestatemanager.core.database.model.AddressEntity
import com.waminiyi.realestatemanager.core.database.model.AgentEntity
import com.waminiyi.realestatemanager.core.database.model.EstateEntity
import com.waminiyi.realestatemanager.core.database.model.PhotoEntity
import com.waminiyi.realestatemanager.core.model.data.EstateType
import com.waminiyi.realestatemanager.core.model.data.Location
import com.waminiyi.realestatemanager.core.model.data.Status
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideRemDatabase(
        @ApplicationContext context: Context
    ): RemDatabase = Room.databaseBuilder(
        context,
        RemDatabase::class.java,
        "rem-database"
    ).addCallback(object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Populate your database with initial data here
            CoroutineScope(Dispatchers.IO).launch {
                // Example: Insert some initial estates
                val estateDao = provideRemDatabase(context).estateDao()
                val agentDao = provideRemDatabase(context).agentDao()
                val photoDao = provideRemDatabase(context).imageDao()

                agentDao.upsertAgent(
                    AgentEntity(
                        agentUuid = UUID.fromString("39bd0f42-d6a1-4968-8971-07b27b08ee95"),
                        "",
                        "",
                        "", "", ""
                    ).also { Log.d("WRITETODB", it.toString()) }
                )

                getInitialEstates().forEach {
                    estateDao.upsertEstate(it)
                    photoDao.upsertPhoto(
                        PhotoEntity(
                            photoUuid = UUID.randomUUID(),
                            estateUuid =it.estateUuid,
                            url = "url",
                            localPath = "path",
                            isMainPhoto = true
                        ))
                    Log.d("WRITETODB", it.toString())
                }

            }
        }
    })
        .build()

    private fun getInitialEstates(): List<EstateEntity> {

        val address1 = AddressEntity(
            streetNumber = 123,
            streetName = "Main Street",
            city = "Cityville",
            state = "Stateville",
            postalCode = 12345,
            location = Location(latitude = 40.7128, longitude = -74.0060)
        )

        val address2 = AddressEntity(
            streetNumber = 456,
            streetName = "Broadway",
            city = "Metro City",
            state = "Stateville",
            postalCode = 67890,
            location = Location(latitude = 34.0522, longitude = -118.2437)
        )

        return listOf(
            EstateEntity(
                estateUuid = UUID.randomUUID(),
                type = EstateType.APARTMENT,
                price = 100000,
                area = 80.0f,
                roomsCount = 4 ,
                description = "Beautiful Apartment",
                addressEntity = address1,
                agentId = UUID.fromString("39bd0f42-d6a1-4968-8971-07b27b08ee95"),
                status = Status.AVAILABLE,
                entryDate = Date(System.currentTimeMillis())


            ),
            EstateEntity(
                estateUuid = UUID.randomUUID(),
                type = EstateType.HOUSE,
                price = 200000,
                area = 150.0f,
                roomsCount = 6,
                description = "Spacious House",
                addressEntity = address2,
                agentId = UUID.fromString("39bd0f42-d6a1-4968-8971-07b27b08ee95"),
                status = Status.AVAILABLE,
                entryDate = Date(System.currentTimeMillis())
            ),
        )
    }
}