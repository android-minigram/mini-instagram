package com.motawfik.minigram.data

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.motawfik.minigram.models.Post
import kotlinx.coroutines.tasks.await

class FirebaseFirestore {
    private val firestore: FirebaseFirestore by lazy {
        Firebase.firestore
    }
    private val firebaseAuth = FirebaseAuth()

    suspend fun createPost(post: Post) {
        val user = firestore.collection("users")
            .document(post.uid)
            .get().await()
        post.userFullName = user.data?.get("name").toString()
        firestore.collection("posts")
            .add(post.addToFirestore())
            .await()
    }

    suspend fun getPosts(): List<Post> {
        val data = firestore.collection("posts")
            .get().await()
        return data.toObjects(Post::class.java)
    }

    fun likePost(postID: String) {
        firestore.collection("posts").document(postID)
            .update("likedBy", FieldValue.arrayUnion(firebaseAuth.currentUserID()))
    }
}