package com.motawfik.minigram.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.motawfik.minigram.databinding.ListItemLikeBinding
import com.motawfik.minigram.databinding.ListItemProfileBinding
import com.motawfik.minigram.likes.LikesAdapter
import com.motawfik.minigram.models.Post


class ProfileGridAdapter(private val onClickListener: PostListener): ListAdapter<Post, ProfileGridAdapter.MyViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val post = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(post)
        }
        holder.bind(post, onClickListener)
    }

    class MyViewHolder(private var binding: ListItemProfileBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post, clickListener: PostListener) {
            binding.post = post
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ProfileGridAdapter.MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemProfileBinding.inflate(layoutInflater, parent, false)
                return ProfileGridAdapter.MyViewHolder(binding)
            }
        }
    }
}

class PostDiffCallback: DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }
}

class PostListener(val clickListener: (post:Post) -> Unit) {
    fun onClick(post: Post) = clickListener(post)
}