package com.waminiyi.realestatemanager.database

import android.content.Context
import androidx.room.Room
import com.waminiyi.realestatemanager.core.database.RemDatabase
import com.waminiyi.realestatemanager.core.di.DatabaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object DatabaseModuleTest {
    @Provides
    @Singleton
    fun provideRemTestDatabase(
        @ApplicationContext context: Context
    ): RemDatabase = Room.inMemoryDatabaseBuilder(context, RemDatabase::class.java)
        .allowMainThreadQueries()
        .build()
}