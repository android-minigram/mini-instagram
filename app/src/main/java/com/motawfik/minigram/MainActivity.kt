package com.motawfik.minigram

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.motawfik.minigram.data.FirebaseAuth
import com.motawfik.minigram.data.FirebaseFirestore


class MainActivity : AppCompatActivity() {
    private val firebaseAuth = FirebaseAuth()
    private val firebaseFirestore = FirebaseFirestore()
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Minigram)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createChannel(
            getString(R.string.likes_notification_channel_id),
            getString(R.string.likes_notification_channel_name)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                if (firebaseAuth.isLoggedIn()) {
                    firebaseFirestore.deleteFCMToken()
                    firebaseAuth.logout()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Likes gained by users"

            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}