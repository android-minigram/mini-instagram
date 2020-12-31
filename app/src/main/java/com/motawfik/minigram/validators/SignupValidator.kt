package com.motawfik.minigram.validators

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.motawfik.minigram.databinding.SignupFragmentBinding

class SignupValidator(private val binding: SignupFragmentBinding) {
    private var validName = MutableLiveData(false)
    private var validPasswords = MutableLiveData(false)
    private var validEmail = MutableLiveData(false)

    var validForm = Transformations.switchMap(validName) {name ->
        Transformations.switchMap(validPasswords) { passwords ->
            Transformations.map(validEmail) { email ->
                name && passwords && email
            }
        }
    }

    init {
        binding.fullNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validName()
            }
        })

        binding.confirmPasswordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validPasswords()
            }
        })

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validPasswords()
            }
        })

        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validEmail()
            }
        })
    }

    private fun validName() {
        val fullName = binding.fullNameEditText.text.toString()
        validName.value = if (fullName.isEmpty()) {
            binding.fullNameEditText.error = "Full name must contain a value"
            false
        } else {
            binding.fullNameEditText.error = null
            true
        }
    }

    private fun validPasswords() {
        val passwordText = binding.passwordEditText.text.toString()
        val confirmPasswordText = binding.confirmPasswordEditText.text.toString()
        validPasswords.value = if (passwordText.isEmpty()) {
            binding.passwordEditText.error = "You must enter a password"
            false
        } else {
            if (passwordText != confirmPasswordText) {
                binding.confirmPasswordEditText.error = "Passwords don't match"
                binding.passwordEditText.error = "Passwords don't match"
                false
            } else {
                binding.confirmPasswordEditText.error = null
                binding.passwordEditText.error = null
                true
            }
        }
    }

    private fun validEmail() {
        val email = binding.emailEditText.text.toString()
        validEmail.value = if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEditText.error = "Invalid email format"
            false
        } else {
            binding.emailEditText.error = null
            true
        }
    }
}