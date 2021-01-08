package com.motawfik.minigram.timeline

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.motawfik.minigram.data.FirebaseAuth
import com.motawfik.minigram.databinding.ListItemPostBinding
import com.motawfik.minigram.models.Post

class TimelineAdapter(private val clickListener: PostListener) : ListAdapter<Post, TimelineAdapter.MyViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class MyViewHolder private constructor(private val binding: ListItemPostBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Post, clickListener: PostListener) {
            binding.post = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemPostBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }
}

class PostDiffCallback: DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
                && oldItem.likedBy.containsAll(newItem.likedBy)
                && newItem.likedBy.containsAll(oldItem.likedBy)
    }

}


class PostListener(val clickListener: (postID: String, authorID: String, like: Boolean) -> Unit) {
    private val firebaseAuth = FirebaseAuth()
    fun onLongClick(post: Post): Boolean {
        val isPostLiked = post.likedBy.contains(firebaseAuth.currentUserID())
        clickListener(post.id, post.uid, isPostLiked)
        return false
    }
    fun onLikeButtonPressed(post: Post) = clickListener(post.id, post.uid, post.likedBy.contains(firebaseAuth.currentUserID()))
}