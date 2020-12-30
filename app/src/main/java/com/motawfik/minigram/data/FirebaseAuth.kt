package com.motawfik.minigram.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.tasks.await

enum class LOGIN_STATUS { NONE, SUCCESS, NO_USER, INVALID_CREDENTIALS, UNKNOWN_ERROR };

class FirebaseAuth {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }


    suspend fun loginWithEmailAndPassword(email: String, password: String): LOGIN_STATUS {
        var loginStatus = LOGIN_STATUS.NONE
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
        } catch (e: FirebaseAuthInvalidUserException) {
            loginStatus = LOGIN_STATUS.NO_USER
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            loginStatus = LOGIN_STATUS.INVALID_CREDENTIALS
        } catch (e: Exception) {
            loginStatus = LOGIN_STATUS.UNKNOWN_ERROR
        }
        return loginStatus
    }


    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser

}