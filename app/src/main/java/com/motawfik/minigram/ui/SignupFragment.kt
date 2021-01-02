package com.motawfik.minigram.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.motawfik.minigram.data.SIGNUP_STATUS
import com.motawfik.minigram.databinding.SignupFragmentBinding
import com.motawfik.minigram.validators.SignupValidator
import com.motawfik.minigram.viewModels.SignupViewModel
import com.motawfik.minigram.viewModels.ViewModelFactory

class SignupFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val application = requireNotNull(this.activity).application
        val signupBinding = SignupFragmentBinding.inflate(inflater)
        signupBinding.lifecycleOwner = this

        val viewModelFactory = ViewModelFactory(application)
        val signupViewModel = ViewModelProvider(this, viewModelFactory).get(SignupViewModel::class.java)
        val signupValidator = SignupValidator(signupBinding)

        signupBinding.viewModel = signupViewModel
        signupBinding.validator = signupValidator

        signupViewModel.signupMessage.observe(viewLifecycleOwner, {
            if (signupViewModel.status.value != SIGNUP_STATUS.NONE) {
                Toast.makeText(activity, it.toString(), Toast.LENGTH_SHORT).show()
                if (signupViewModel.status.value == SIGNUP_STATUS.SUCCESS) {
                    findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToProfileFragment())
                }
                signupViewModel.finishedSignup()
            }
        })

        signupBinding.loginBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        return signupBinding.root
    }
}