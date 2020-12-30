package com.motawfik.minigram.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.motawfik.minigram.data.LOGIN_STATUS
import com.motawfik.minigram.databinding.LoginFragmentBinding
import com.motawfik.minigram.viewModels.LoginViewModel
import com.motawfik.minigram.viewModels.ViewModelFactory

class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = requireNotNull(this.activity).application
        val loginBinding = LoginFragmentBinding.inflate(inflater)
        loginBinding.lifecycleOwner = this

        val loginViewModelFactory = ViewModelFactory(application)
        val loginViewModel =
            ViewModelProvider(this, loginViewModelFactory).get(LoginViewModel::class.java)

        loginBinding.viewModel = loginViewModel
        loginBinding.signUpBtb.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
            findNavController().navigate(action)
        }

        loginViewModel.loginMessage.observe(viewLifecycleOwner, {
            if (loginViewModel.loggedIn.value != LOGIN_STATUS.NONE) {
                Toast.makeText(activity, it.toString(), Toast.LENGTH_SHORT).show()
                loginViewModel.finishedLoggingIn()
            }
        })

        return loginBinding.root
    }

}