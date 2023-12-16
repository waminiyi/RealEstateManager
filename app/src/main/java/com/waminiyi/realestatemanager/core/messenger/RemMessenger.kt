package com.waminiyi.realestatemanager.core.messenger

import com.waminiyi.realestatemanager.core.messenger.model.Message

interface RemMessenger {

    fun postMessage(message: Message)

}