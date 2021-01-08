package com.motawfik.minigram.data

import android.app.NotificationManager
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.motawfik.minigram.R

class FirebaseMessagingService: FirebaseMessagingService() {
    override fun onNewToken(p0: String) {
        val sharedPreferences = applicationContext.getSharedPreferences(
            getString(R.string.shared_prefs_file), MODE_PRIVATE)
        sharedPreferences.edit().putString(getString(R.string.fcm_token_key), p0).apply()
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        p0.notification?.let {
            sendNotification(it.title, it.body)
        }
        super.onMessageReceived(p0)
    }

    private fun sendNotification(title: String?, body: String?) {
        val notificationManager = ContextCompat.getSystemService(
            applicationContext, NotificationManager::class.java
        ) as NotificationManager
        notificationManager.sendNotification(title, body, applicationContext)
    }
}