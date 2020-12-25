package com.motawfik.minigram.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.motawfik.minigram.validation.LoginValidation

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private var loginValidation: LoginValidation = LoginValidation()

    fun validateCredentials(email: String, password:String): LiveData<String> {
        return loginValidation.validateCredentials(email, password)
    }
}