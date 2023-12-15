package com.waminiyi.realestatemanager.core.messenger.model

data class PushNotification(
    val topic: String,
    val notification: Notification,
    val data: Any,
)

data class Notification(
    val title: String,
    val body: String
)