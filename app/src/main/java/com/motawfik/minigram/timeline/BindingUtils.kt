package com.motawfik.minigram.timeline

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.Timestamp
import com.motawfik.minigram.GlideApp
import com.motawfik.minigram.R
import com.motawfik.minigram.data.FirebaseAuth
import com.motawfik.minigram.data.FirebaseStorage
import com.motawfik.minigram.models.Post

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

@BindingAdapter("likeButton")
fun ImageView.setLikeButtonColor(post: Post?) {
    post?.let {
        val auth = FirebaseAuth()
        if (post.likedBy.contains(auth.currentUser()?.uid))
            setBackgroundResource(R.drawable.ic_baseline_favorite_30_red)
        else
            setBackgroundResource(R.drawable.ic_outline_favorite_border_30)
    }
}