package com.motawfik.minigram.models

import com.google.firebase.Timestamp
import kotlin.collections.HashMap

class Post(var path: String, var uid: String) {
    var userFullName = ""
    fun addToFirestore(): HashMap<String, Any> {
        return hashMapOf(
            "uid" to uid,
            "userFullName" to userFullName,
            "path" to path,
            "timestamp" to Timestamp.now()
        )
    }
}