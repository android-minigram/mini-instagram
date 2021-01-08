package com.motawfik.minigram.data

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.motawfik.minigram.R

class FirebaseMessagingService: FirebaseMessagingService() {
    val firebaseFirestore = FirebaseFirestore()

    override fun onNewToken(p0: String) {
        Log.d("NEW_FCM", "From messagingService: $p0")
        val sharedPreferences = applicationContext.getSharedPreferences(
            getString(R.string.shared_prefs_file), MODE_PRIVATE)
        sharedPreferences.edit().putString(getString(R.string.fcm_token_key), p0).apply()
//        firebaseFirestore.saveFCMToken(p0)
    }

}