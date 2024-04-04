package com.waminiyi.realestatemanager.core.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.waminiyi.realestatemanager.R
import kotlin.random.Random

private const val CHANNEL_ID = "Estate_saving_channel"

fun Context.sendNotification(
    channelId: String = CHANNEL_ID,
    notificationId: Int = Random.nextInt(),
    title: String,
    message: String,
    channelName: String = "Default Channel",
    importance: Int = NotificationManager.IMPORTANCE_DEFAULT
) {
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(channelId, channelName, importance)
        notificationManager.createNotificationChannel(notificationChannel)
    }
    val notification = NotificationCompat.Builder(this, channelId)
        .setSmallIcon(R.drawable.app_icon_foreground)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .build()

    notificationManager.notify(notificationId, notification)
}