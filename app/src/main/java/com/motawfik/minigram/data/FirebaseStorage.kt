package com.motawfik.minigram.data

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.util.*

class FirebaseStorage {
    private val storage: FirebaseStorage by lazy {
        Firebase.storage
    }
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    suspend fun uploadNewImage(bytesArray: ByteArray): String {
        val postsRef = storage.reference.child("posts/${auth.currentUser?.uid!!}/${Calendar.getInstance().timeInMillis}")
        val snapshot = postsRef.putBytes(bytesArray).await()
        Log.d("IMAGE_PATH", snapshot.metadata?.path!!)
        return snapshot.metadata?.path!!
    }

    suspend fun uploadSavedImage(uri: Uri): String {
        val postsRef = storage.reference.child("posts/${auth.currentUser?.uid!!}/${Calendar.getInstance().timeInMillis}")
        val snapshot = postsRef.putFile(uri).await()
        Log.d("IMAGE_PATH", snapshot.metadata?.path!!)
        return snapshot.metadata?.path!!
    }
}