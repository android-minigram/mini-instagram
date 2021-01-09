package com.motawfik.minigram.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.motawfik.minigram.data.FirebaseFirestore
import com.motawfik.minigram.models.Post
import com.motawfik.minigram.models.UserBasicData
import kotlinx.coroutines.*

class ProfileViewModel : ViewModel() {
    private val firebaseFirestore = FirebaseFirestore()

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel() // to cancel all coroutines when the view model is terminated
    }

    private val _userPosts = MutableLiveData<List<Post>>()
    val userPosts: LiveData<List<Post>>
        get() = _userPosts

    private val _user = MutableLiveData<UserBasicData>()
    val user: LiveData<UserBasicData>
        get() = _user

    fun getPostsByUserID(userID: String) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                _userPosts.postValue(firebaseFirestore.getPostsByUserID(userID))
            }
        }
    }

    fun getUserByUserID(userID: String) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                _user.postValue(firebaseFirestore.getUsersByIDs(listOf(userID))[0])
            }
        }
    }
}