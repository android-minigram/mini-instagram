package com.motawfik.minigram.signup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.motawfik.minigram.data.FirebaseAuth
import com.motawfik.minigram.data.SIGNUP_STATUS
import kotlinx.coroutines.*

class SignupViewModel(application: Application) : AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel() // to cancel all coroutines when the view model is terminated
    }

    var userData = MutableLiveData<NewUser>()

    init {
        userData.value = NewUser()
    }

    private var _status = MutableLiveData<SIGNUP_STATUS>()
    val status: LiveData<SIGNUP_STATUS>
        get() = _status

    var signupMessage = Transformations.map(_status) {
        when (it) {
            SIGNUP_STATUS.SUCCESS -> "Registered Successfully"
            SIGNUP_STATUS.DUPLICATE_EMAIL -> "Duplicate Email"
            SIGNUP_STATUS.MALFORMED_EMAIL -> "Wrongly Formatted Email"
            SIGNUP_STATUS.WEAK_PASSWORD -> "Weak Password"
            SIGNUP_STATUS.UNKNOWN_ERROR -> "Unknown Error Occurred"
            else -> ""
        }
    }

    private var _registerLoading = MutableLiveData<Boolean>()
    val registerLoading: LiveData<Boolean>
        get() = _registerLoading

    private val firebaseAuth = FirebaseAuth()
    fun register() {
        uiScope.launch {
            _registerLoading.value = true
            var signupStatus: SIGNUP_STATUS
            withContext(Dispatchers.IO) {
                signupStatus = firebaseAuth.registerWithEmailAndPassword(
                    userData.value!!
                )
            }
            _status.value = signupStatus
            _registerLoading.value = false
        }
    }

    fun finishedSignup() {
        _status.value = SIGNUP_STATUS.NONE
    }
}