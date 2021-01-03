package com.motawfik.minigram.data

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.motawfik.minigram.models.Post
import kotlinx.coroutines.tasks.await
import java.util.*

class FirebaseStorage {
    private val storage: FirebaseStorage by lazy {
        Firebase.storage
    }
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private val firestore = FirebaseFirestore()

    suspend fun uploadNewImage(bytesArray: ByteArray): String {
        val uid = auth.currentUser?.uid!!
        val postsRef = storage.reference.child("/posts/$uid/${Calendar.getInstance().timeInMillis}")
        postsRef.putBytes(bytesArray).await()
        createPost(postsRef.path, uid)
        return postsRef.path
    }

    suspend fun uploadSavedImage(uri: Uri): String {
        val uid = auth.currentUser?.uid!!
        val postsRef = storage.reference.child("/posts/$uid/${Calendar.getInstance().timeInMillis}")
        postsRef.putFile(uri).await()
        createPost(postsRef.path, uid)
        return postsRef.path
    }

    private suspend fun createPost(path: String, uid: String) {
        val post = Post(path, uid)
        firestore.createPost(post)
    }
}