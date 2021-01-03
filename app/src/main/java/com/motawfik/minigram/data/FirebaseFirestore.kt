package com.motawfik.minigram.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.motawfik.minigram.models.Post
import kotlinx.coroutines.tasks.await

class FirebaseFirestore {
    private val firestore: FirebaseFirestore by lazy {
        Firebase.firestore
    }

    suspend fun createPost(post: Post) {
        val user = firestore.collection("users")
            .document(post.uid)
            .get().await()
        post.userFullName = user.data?.get("name").toString()
        firestore.collection("posts")
            .add(post.addToFirestore())
            .await()
    }
}