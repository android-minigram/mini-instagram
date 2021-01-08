package com.motawfik.minigram.data

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.motawfik.minigram.MainActivity
import com.motawfik.minigram.R

private val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(title: String?, body: String?, applicationContext: Context) {
    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT
    )
    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.likes_notification_channel_id)
    )

        .setSmallIcon(R.drawable.logo)
        .setContentTitle(title)
        .setContentText(body)

        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    notify(NOTIFICATION_ID, builder.build())

}