package com.motawfik.minigram.models

import com.google.firebase.firestore.DocumentId

class Notification {
    @DocumentId
    var id = ""
    var postID = ""
    var likedByUserID = ""
    var authorID = ""
    var type = ""

    fun addToFirestore(): HashMap<String, String> {
        return hashMapOf(
            "postID" to postID,
            "likedByUserID" to likedByUserID,
            "authorID" to authorID,
        )
    }

    constructor()
    constructor(postID: String, userID: String, authorID: String, type: String) {
        this.postID = postID
        this.likedByUserID = userID
        this.authorID = authorID
        this.type = type
    }
}