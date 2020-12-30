package com.motawfik.minigram.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(val app: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(app) as T
        }
        if (modelClass.isAssignableFrom(SignupViewModel::class.java)) {
            return SignupViewModel(app) as T
        }
        throw IllegalArgumentException("UNABLE TO CONSTRUCT VIEW MODEL")
    }

}