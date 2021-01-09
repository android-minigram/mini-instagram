package com.motawfik.minigram.models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserBasicData(): Parcelable {
    @DocumentId
    var uid = ""
    var name = ""

    constructor(name: String) : this() {
        this.name = name
    }
    constructor(uid: String, name: String) : this() {
        this.uid = uid
        this.name = name
    }
}