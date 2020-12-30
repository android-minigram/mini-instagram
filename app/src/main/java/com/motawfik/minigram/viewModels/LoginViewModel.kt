package com.motawfik.minigram.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.motawfik.minigram.data.FirebaseAuth
import com.motawfik.minigram.data.LOGIN_STATUS
import kotlinx.coroutines.*

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel() // to cancel all coroutines when the view model is terminated
    }

    private var _loggedIn = MutableLiveData<LOGIN_STATUS>()
    val loggedIn: LiveData<LOGIN_STATUS>
        get() = _loggedIn

    var loginMessage = Transformations.map(_loggedIn) {
        when (it) {
            LOGIN_STATUS.SUCCESS -> "LOGGED IN"
            LOGIN_STATUS.NO_USER -> "NO USER FOUND"
            LOGIN_STATUS.INVALID_CREDENTIALS -> "INVALID USERNAME/PASSWORD"
            LOGIN_STATUS.UNKNOWN_ERROR -> "UNKNOWN ERROR FOUND"
            else -> ""
        }
    }

    private var _loginLoading = MutableLiveData<Boolean>()
    val loginLoading: LiveData<Boolean>
        get() = _loginLoading

    private val firebaseAuth = FirebaseAuth()

    init {
        email.value = ""
        password.value = ""

    }

    fun login() {
        uiScope.launch {
            _loginLoading.value = true
            var status: LOGIN_STATUS
            withContext(Dispatchers.IO) {
                status = firebaseAuth.loginWithEmailAndPassword(email.value!!, password.value!!)
            }
            _loggedIn.value = status
            _loginLoading.value = false
        }
    }

    fun finishedLoggingIn() {
        _loggedIn.value = LOGIN_STATUS.NONE
    }
}