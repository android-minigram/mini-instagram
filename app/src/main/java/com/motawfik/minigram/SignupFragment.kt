package com.motawfik.minigram

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

class SignupFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_signup, container, false)
        val loginBtn = rootView.findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        return rootView
    }
}