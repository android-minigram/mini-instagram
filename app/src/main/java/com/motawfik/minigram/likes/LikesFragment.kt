package com.motawfik.minigram.likes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val usersIDs = args.usersIDs
        val imagePath = args.imagePath
        val imageView = binding.selectedImage

        GlideApp.with(imageView.context)
            .load(storage.getPathReference(imagePath))
            .apply(
                RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image)).into(imageView)

        viewModel.getUsersByIDs(usersIDs)

        val adapter = LikesAdapter(LikeListener { userID ->
            Log.d("LIKES_FRAGMENT", "CLICKED $userID")
        })
        binding.likesList.adapter = adapter
        viewModel.users.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }
}