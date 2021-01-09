package com.motawfik.minigram.likes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.request.RequestOptions
import com.motawfik.minigram.GlideApp
import com.motawfik.minigram.R
import com.motawfik.minigram.data.FirebaseStorage
import com.motawfik.minigram.databinding.FragmentLikesBinding

class LikesFragment : Fragment() {
    private val args: LikesFragmentArgs by navArgs()
    private val storage = FirebaseStorage()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLikesBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val viewModel = LikesViewModel()
        binding.viewModel = viewModel

        val postID = args.postID
        viewModel.getPostByID(postID)

        val imageView = binding.selectedImage
        val adapter = LikesAdapter(LikeListener { userID ->
            val action = LikesFragmentDirections.actionLikesFragmentToProfileFragment(userID)
            findNavController().navigate(action)
        })
        binding.likesList.adapter = adapter
        viewModel.users.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })
        viewModel.post.observe(viewLifecycleOwner, {
            it?.let {
                viewModel.getUsersByIDs(it.likedBy)
                GlideApp.with(imageView.context)
                    .load(storage.getPathReference(it.path))
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.loading_animation)
                            .error(R.drawable.ic_broken_image)
                    ).into(imageView)
            }
        })

        return binding.root
    }
}