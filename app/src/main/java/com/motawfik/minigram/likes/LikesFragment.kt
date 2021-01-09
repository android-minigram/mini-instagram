package com.motawfik.minigram.likes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.motawfik.minigram.databinding.FragmentLikesBinding

class LikesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("LIKES_FRAGMENT", "CREATED")
        val binding = FragmentLikesBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val viewModel = LikesViewModel()
        binding.viewModel = viewModel

        val adapter = LikesAdapter(LikeListener { userID ->
            Log.d("LIKES_FRAGMENT", "CLICKED $userID")
        })
        binding.likesList.adapter = adapter
        viewModel.users.observe(viewLifecycleOwner, {
            Log.d("LIKES_FRAGMENT", "LIST OBSERVED")
            it?.let {
                Log.d("LIKES_FRAGMENT", "LIST NOT NULL")
                adapter.submitList(it)
            }
        })

        return binding.root
    }
}