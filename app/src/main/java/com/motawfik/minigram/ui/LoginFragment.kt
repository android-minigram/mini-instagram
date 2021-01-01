package com.motawfik.minigram.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.internal.GoogleApiManager
import com.google.firebase.auth.GoogleAuthProvider
import com.motawfik.minigram.R
import com.motawfik.minigram.data.LOGIN_STATUS
import com.motawfik.minigram.databinding.LoginFragmentBinding
import com.motawfik.minigram.viewModels.LoginViewModel
import com.motawfik.minigram.viewModels.ViewModelFactory

class LoginFragment : Fragment() {
    private var loginViewModel: LoginViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = requireNotNull(this.activity).application
        val loginBinding = LoginFragmentBinding.inflate(inflater)
        loginBinding.lifecycleOwner = this

        val loginViewModelFactory = ViewModelFactory(application)

        loginViewModel = ViewModelProvider(this, loginViewModelFactory).get(LoginViewModel::class.java)

        loginBinding.viewModel = loginViewModel
        loginBinding.signUpBtb.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
            findNavController().navigate(action)
        }

        loginViewModel!!.loginMessage.observe(viewLifecycleOwner, {
            if (loginViewModel!!.loggedIn.value != LOGIN_STATUS.NONE) {
                Toast.makeText(activity, it.toString(), Toast.LENGTH_SHORT).show()
                loginViewModel!!.finishedLoggingIn()
            }
        })

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        loginViewModel!!.googleLogin.observe(viewLifecycleOwner, {
            if (it == true) {
                // Configure Google Sign In
                val googleLoginIntent = googleSignInClient.signInIntent
                resultLauncher.launch(googleLoginIntent)
                loginViewModel!!.finishLoggingWithGoogle()
            }
        })

        return loginBinding.root
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            loginViewModel!!.loginWithGoogle(task)
        }
    }

}