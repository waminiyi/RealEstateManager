package com.waminiyi.realestatemanager.data.models

data class Photo(
    val uuid: String,
    val estateUuid: String,
    val localPath: String? = null,
    val remoteUrl: String? = null,
    val isMain: Boolean = false,
    val description: String? = null
)
