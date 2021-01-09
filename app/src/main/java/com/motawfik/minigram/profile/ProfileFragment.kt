package com.motawfik.minigram.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.motawfik.minigram.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private val args: ProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val profileBinding = FragmentProfileBinding.inflate(inflater)
        profileBinding.lifecycleOwner = this

        val viewModel = ProfileViewModel()
        profileBinding.viewModel = viewModel

        val adapter = ProfileGridAdapter(PostListener { post ->
            val action = ProfileFragmentDirections.actionProfileFragmentToLikesFragment(post.id)
            findNavController().navigate(action)
        })
        profileBinding.photosGrid.adapter = adapter

        val userID = args.userID
        viewModel.getPostsByUserID(userID)
        viewModel.getUserByUserID(userID)

        viewModel.userPosts.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

        return profileBinding.root
    }
}