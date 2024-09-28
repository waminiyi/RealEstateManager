package com.waminiyi.realestatemanager

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class RemApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Places.initialize(applicationContext, BuildConfig.GOOGLE_PLACES_API_KEY)
    }

}