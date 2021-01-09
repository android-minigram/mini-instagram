package com.motawfik.minigram.likes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.motawfik.minigram.models.UserBasicData

class LikesViewModel : ViewModel() {
    private val _users = MutableLiveData<List<UserBasicData>>()
    val users: LiveData<List<UserBasicData>>
        get() = _users

    init {
        val myList = listOf(
            UserBasicData("id1", "User 1"),
            UserBasicData("id2", "User 2"),
            UserBasicData("id3", "User 3"),
            UserBasicData("id4", "User 4"),
            UserBasicData("id5", "User 5"),
        )
        _users.value = myList
    }
}