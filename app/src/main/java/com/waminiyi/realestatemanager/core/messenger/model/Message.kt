package com.waminiyi.realestatemanager.core.messenger.model

data class Message(
    val `data`: Any,
    val `notification`: NotificationX,
    val `topic`: String,
)

data class TargetMessage(
    val `data`: Any,
    val `notification`: NotificationX,
    val `token`: String,
)


