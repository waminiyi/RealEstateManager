package com.waminiyi.realestatemanager.core.util.util

import java.io.InputStream
import java.util.Properties

class PropertiesUtil {
    fun loadProperties(): Properties {
        val properties = Properties()
        val inputStream: InputStream? =
            this::class.java.classLoader?.getResourceAsStream("config.properties")
        inputStream?.let { properties.load(it ) }
        return properties
    }
}

