package com.waminiyi.realestatemanager.core.messenger

import com.waminiyi.realestatemanager.core.messenger.model.RemMessage


interface RemMessenger {

    fun postMessage(message: RemMessage)

}