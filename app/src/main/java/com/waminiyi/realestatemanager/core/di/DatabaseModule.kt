package com.waminiyi.realestatemanager.core.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.waminiyi.realestatemanager.core.Constants.REM_DATABASE_NAME
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
        REM_DATABASE_NAME
    ).addCallback(object : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            db.execSQL("PRAGMA foreign_keys=ON;")
        }
    }).setQueryCallback(
        { sql, bindArgs -> Log.d("RoomSQL", "Query: $sql, BindArgs: $bindArgs") },
        { command -> command.run() }
    ).build()
}