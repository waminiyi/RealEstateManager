package com.waminiyi.realestatemanager.core.utils.cities

import android.content.Context
import com.waminiyi.realestatemanager.R
import com.waminiyi.realestatemanager.core.utils.dispatchers.Dispatcher
import com.waminiyi.realestatemanager.core.utils.dispatchers.RemDispatchers
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.IOException
import javax.inject.Inject

class CitiesUtils @Inject constructor(
    @ApplicationContext private val context: Context,
    @Dispatcher(RemDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) {
    private var cities: List<String> = emptyList()

    suspend fun getUsCities(): List<String> {
        return when {
            cities.isEmpty() -> withContext(ioDispatcher) {
                try {
                    val inputStream = context.resources.openRawResource(R.raw.us_cities)
                    val content = inputStream.bufferedReader().use(BufferedReader::readText)
                    inputStream.close()
                    Json.decodeFromString<List<String>>(content)
                } catch (e: IOException) {
                    e.printStackTrace()
                    emptyList()
                }
            }

            else -> cities
        }
    }
}