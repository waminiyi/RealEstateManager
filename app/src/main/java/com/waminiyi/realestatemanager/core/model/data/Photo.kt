package com.waminiyi.realestatemanager.core.model.data

data class Photo(
    val uuid: String,
    val estateUuid: String,
    val localPath: String? = null,
    val remoteUrl: String? = null,
    val isMain: Boolean = false
)
