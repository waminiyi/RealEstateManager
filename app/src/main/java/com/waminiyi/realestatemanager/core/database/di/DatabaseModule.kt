package com.waminiyi.realestatemanager.core.database.di

import android.content.Context
import androidx.room.Room
import com.waminiyi.realestatemanager.core.database.RemDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
    ).build()
}