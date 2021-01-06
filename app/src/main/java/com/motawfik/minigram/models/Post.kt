package com.motawfik.minigram.models

import com.google.firebase.Timestamp
import kotlin.collections.HashMap

class Post {
    var path = ""
    var uid = ""
    var id = ""
    var userFullName = ""
    lateinit var timestamp: Timestamp
    fun addToFirestore(): HashMap<String, Any> {
        return hashMapOf(
            "uid" to uid,
            "userFullName" to userFullName,
            "path" to path,
            "timestamp" to Timestamp.now()
        )
    }


    constructor()
    constructor(path: String, uid: String) {
        this.path = path
        this.uid = uid
    }

    override fun equals(other: Any?) =
        (other is Post)
                && path == other.path
                && uid == other.uid
}