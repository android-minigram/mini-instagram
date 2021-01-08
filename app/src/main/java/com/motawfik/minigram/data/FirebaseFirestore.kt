package com.motawfik.minigram.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>>
        get() = _posts

    init {
        getPosts()
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

    private fun getPosts() {
        firestore.collection("posts").addSnapshotListener { data, error ->
            if (error != null) {
                Log.w("SNAPSHOT_ERROR", error)
                return@addSnapshotListener
            }
            _posts.value = data?.toObjects(Post::class.java)
        }

    }

    fun likePost(postID: String) {
        firestore.collection("posts").document(postID)
            .update("likedBy", FieldValue.arrayUnion(firebaseAuth.currentUserID()))
    }
    fun unlikePost(postID: String) {
        firestore.collection("posts").document(postID)
            .update("likedBy", FieldValue.arrayRemove(firebaseAuth.currentUserID()))
    }
}