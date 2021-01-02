package com.motawfik.minigram.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.motawfik.minigram.R
import com.motawfik.minigram.data.FirebaseAuth

class SplashFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val firebaseAuth = FirebaseAuth()
        if (firebaseAuth.isLoggedIn()) {
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToProfileFragment())
        } else {
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
        }

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
}