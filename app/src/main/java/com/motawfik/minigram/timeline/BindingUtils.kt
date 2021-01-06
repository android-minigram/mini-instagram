package com.motawfik.minigram.timeline

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.Timestamp
import com.motawfik.minigram.GlideApp
import com.motawfik.minigram.data.FirebaseStorage

@BindingAdapter("displayImage")
fun ImageView.bindImage(imgPath: String?) {
    val storage = FirebaseStorage()
    imgPath?.let {
        val reference = storage.getPathReference(imgPath)
        GlideApp.with(this.context)
            .load(reference)
            .into(this)
    }
}

@BindingAdapter("timePassed")
fun TextView.setTimePassed(timestamp: Timestamp) {
    text = TimeAgo.using(timestamp.toDate().time)
}