package com.waminiyi.realestatemanager.core.messenger.model

data class RemMessage(
    val message: HashMap<String, Any> = hashMapOf()
) {
    fun setToken(token: String): RemMessage {
        message["token"] = token
        // Remove topic if it was set previously
        message.remove("topic")
        return this
    }

    fun setTopic(topic: String): RemMessage {
        message["topic"] = topic
        // Remove token if it was set previously
        message.remove("token")
        return this
    }

    fun setNotification(title: String, body: String): RemMessage {
        val notification = HashMap<String, String>()
        notification["title"] = title
        notification["body"] = body
        message["notification"] = notification
        return this
    }

    fun setData(data: Any): RemMessage {
        message["data"] = data
        return this
    }

    fun setAndroidConfig(timeToLive: String?, clickAction: String?, priority: String?): RemMessage {
        val androidConfig = HashMap<String, Any>()
        timeToLive?.let { androidConfig["ttl"] = it }
        clickAction?.let {
            val notification = HashMap<String, String>()
            notification["click_action"] = clickAction
            androidConfig["notification"] = notification
        }
        priority?.let { androidConfig["priority"] = priority }
        if (androidConfig.isNotEmpty()) {
            message["android"] = androidConfig
        }
        return this
    }
}
