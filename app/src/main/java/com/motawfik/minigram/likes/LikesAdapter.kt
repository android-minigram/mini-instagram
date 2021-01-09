package com.motawfik.minigram.likes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.motawfik.minigram.databinding.ListItemLikeBinding
import com.motawfik.minigram.models.UserBasicData

class LikesAdapter(private val clickListener: LikeListener) : ListAdapter<UserBasicData, LikesAdapter.MyViewHolder>(LikeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class MyViewHolder private constructor(private val binding: ListItemLikeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserBasicData, clickListener: LikeListener) {
            binding.user = user
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemLikeBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }
}

class LikeDiffCallback: DiffUtil.ItemCallback<UserBasicData>() {
    override fun areItemsTheSame(oldItem: UserBasicData, newItem: UserBasicData): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: UserBasicData, newItem: UserBasicData): Boolean {
        return oldItem.name == newItem.name
    }

}

class LikeListener(val clickListener: (userID: String) -> Unit) {
    fun onNamePressed(user: UserBasicData) = clickListener(user.uid)
}