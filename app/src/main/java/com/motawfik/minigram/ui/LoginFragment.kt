package com.motawfik.minigram.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.motawfik.minigram.R
import com.motawfik.minigram.databinding.LoginFragmentBinding
import com.motawfik.minigram.viewModels.LoginViewModel
import com.motawfik.minigram.viewModels.ViewModelFactory

class LoginFragment : Fragment() {
    lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val application = requireNotNull(this.activity).application
        val viewModelFactory = ViewModelFactory(application)
        loginViewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val loginBinder: LoginFragmentBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_login, container, false)
        val loginView = loginBinder.root
        loginBinder.signUpBtb.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
            findNavController().navigate(action)
        }
        loginBinder.signInBtn.setOnClickListener {
            Log.d("LoginDatabinding", loginBinder.emailEditText.text.toString())
            val email: String = loginBinder.emailEditText.text.toString()
            val password: String = loginBinder.passwordEditText.text.toString()
            loginViewModel.validateCredentials(email, password).observe(viewLifecycleOwner, {
                it.let {
                    Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
                }
            })
        }
        return loginView
    }

}