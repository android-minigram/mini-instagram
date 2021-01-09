package com.motawfik.minigram.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.motawfik.minigram.R
import com.motawfik.minigram.data.FACEBOOK_LOGIN_STATUS
import com.motawfik.minigram.data.GOOGLE_LOGIN_STATUS
import com.motawfik.minigram.data.LOGIN_STATUS
import com.motawfik.minigram.databinding.LoginFragmentBinding
import com.motawfik.minigram.viewModels.ViewModelFactory

class LoginFragment : Fragment() {
    private var loginViewModel: LoginViewModel? = null
    private lateinit var callbackManager: CallbackManager

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
                if (loginViewModel!!.loggedIn.value == LOGIN_STATUS.SUCCESS) {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToTimelineFragment())
                }
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

        loginViewModel!!.facebookLoginMessage.observe(viewLifecycleOwner, {
            if (loginViewModel!!.facebookStatus.value != FACEBOOK_LOGIN_STATUS.NONE) {
                Toast.makeText(activity, it.toString(), Toast.LENGTH_LONG).show()
                if (loginViewModel!!.facebookStatus.value == FACEBOOK_LOGIN_STATUS.SUCCESS)
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToTimelineFragment())
                loginViewModel!!.finishFacebookLogin()
            }
        })

        loginViewModel!!.googleLoginMessage.observe(viewLifecycleOwner, {
            if (loginViewModel!!.googleStatus.value != GOOGLE_LOGIN_STATUS.NONE) {
                Toast.makeText(activity, it.toString(), Toast.LENGTH_LONG).show()
                if (loginViewModel!!.googleStatus.value == GOOGLE_LOGIN_STATUS.SUCCESS)
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToTimelineFragment())
                loginViewModel!!.finishLoggingWithGoogle()
            }
        })

        callbackManager = CallbackManager.Factory.create()

        loginBinding.facebookImgBtn.setOnClickListener {
            LoginManager.getInstance()
                .logInWithReadPermissions(this, listOf("email", "public_profile"))

            LoginManager.getInstance()
                .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult) {
                        loginViewModel!!.loginWithFacebook(result)
                    }

                    override fun onCancel() {
                        Toast.makeText(
                            activity,
                            "Facebook login operation canceled",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onError(error: FacebookException?) {
                        Toast.makeText(
                            activity,
                            "Error while logging in with facebook",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }

        return loginBinding.root
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            loginViewModel!!.loginWithGoogle(task)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}