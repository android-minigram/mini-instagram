package com.motawfik.minigram.likes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.motawfik.minigram.data.FirebaseFirestore
import com.motawfik.minigram.models.Post
import com.motawfik.minigram.models.UserBasicData
import kotlinx.coroutines.*

class LikesViewModel : ViewModel() {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel() // to cancel all coroutines when the view model is terminated
    }

    private val firebaseFireStore = FirebaseFirestore()

    private val _users = MutableLiveData<List<UserBasicData>>()
    val users: LiveData<List<UserBasicData>>
        get() = _users

    private val _post = MutableLiveData<Post>()
    val post: LiveData<Post>
        get() = _post

    fun getUsersByIDs(usersIDs: List<String>) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                _users.postValue(firebaseFireStore.getUsersByIDs(usersIDs))
            }
        }
    }

    fun getPostByID(postID: String) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                _post.postValue(firebaseFireStore.getPostByID(postID))
            }
        }
    }
}