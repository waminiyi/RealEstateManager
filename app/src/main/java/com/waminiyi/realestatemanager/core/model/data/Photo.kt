package com.waminiyi.realestatemanager.core.model.data

sealed class Photo(
    open val uuid: String,
    open val description: String,
    open val url: String
) {
    data class MainPhoto(
        override val uuid: String,
        override val description: String,
        override val url: String,
        val localPath: String
    ) : Photo(uuid, description, url)

    data class AdditionalPhoto(
        override val uuid: String,
        override val description: String,
        override val url: String,
    ) : Photo(uuid, description, url)
}
