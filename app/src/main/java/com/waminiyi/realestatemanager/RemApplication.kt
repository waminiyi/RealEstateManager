package com.waminiyi.realestatemanager

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.soloader.SoLoader
import com.google.android.libraries.places.api.Places
import com.waminiyi.realestatemanager.core.messenger.Constants.GOOGLE_PLACES_API_KEY
import com.waminiyi.realestatemanager.core.sync.status.SyncSubscriber
import com.waminiyi.realestatemanager.core.sync.initializers.Sync
import com.waminiyi.realestatemanager.core.util.util.PropertiesUtil
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class RemApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        //Sync.initialize(this)
        val configProperties = PropertiesUtil().loadProperties()
        val apiKey = configProperties.getProperty(GOOGLE_PLACES_API_KEY)
        Places.initialize(applicationContext, apiKey)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}