package com.motawfik.minigram.timeline

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.motawfik.minigram.data.FirebaseFirestore
import com.motawfik.minigram.data.FirebaseStorage
import com.motawfik.minigram.models.Post
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream

class TimelineViewModel(application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val firebaseStorage = FirebaseStorage()
    private val firebaseFirestore = FirebaseFirestore()
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel() // to cancel all coroutines when the view model is terminated
    }

    private var _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>>
        get() = _posts

    fun uploadNewImage(bitmap: Bitmap) {
        val bytesStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytesStream)
        val data = bytesStream.toByteArray()
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val path = firebaseStorage.uploadNewImage(data)
            }
        }
    }

    fun uploadSavedImage(uri: Uri) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val path = firebaseStorage.uploadSavedImage(uri)
            }
        }
    }

    fun getPosts() {
        uiScope.launch {
            var retrievedPosts: List<Post>
            withContext(Dispatchers.IO) {
                retrievedPosts = firebaseFirestore.getPosts()
            }
            _posts.value = retrievedPosts
        }
    }

    fun likePost(postID: String) {
        firebaseFirestore.likePost(postID)
    }
}