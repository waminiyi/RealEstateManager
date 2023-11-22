package com.waminiyi.realestatemanager.core.data.remote.model

import com.google.firebase.firestore.PropertyName


data class RemotePhoto(
    val uuid: String,
    @PropertyName("estateUuid")
    val estateUuid: String,
    val description: String,
    val url: String,
    @field:JvmField
    val isMainImage: Boolean
)

