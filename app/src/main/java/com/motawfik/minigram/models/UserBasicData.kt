package com.motawfik.minigram.models

import com.google.firebase.firestore.DocumentId

class UserBasicData {
    @DocumentId
    var uid = ""
    var fullName = ""

    constructor()
    constructor(fullName: String) {
        this.fullName = fullName
    }
    constructor(uid: String, fullName: String) {
        this.uid = uid
        this.fullName = fullName
    }
}