package com.motawfik.minigram.data

import com.google.firebase.auth.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

enum class LOGIN_STATUS { NONE, SUCCESS, NO_USER, INVALID_CREDENTIALS, UNKNOWN_ERROR };
enum class SIGNUP_STATUS { NONE, SUCCESS, DUPLICATE_EMAIL, MALFORMED_EMAIL, WEAK_PASSWORD, UNKNOWN_ERROR };

class FirebaseAuth {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }


    suspend fun loginWithEmailAndPassword(email: String, password: String): LOGIN_STATUS {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            LOGIN_STATUS.SUCCESS
        } catch (e: FirebaseAuthInvalidUserException) {
            LOGIN_STATUS.NO_USER
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            LOGIN_STATUS.INVALID_CREDENTIALS
        } catch (e: Exception) {
            LOGIN_STATUS.UNKNOWN_ERROR
        }
    }

    suspend fun registerWithEmailAndPassword(email: String, password: String): SIGNUP_STATUS {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            SIGNUP_STATUS.SUCCESS
        } catch (e: FirebaseAuthUserCollisionException) {
            SIGNUP_STATUS.DUPLICATE_EMAIL
        } catch (e: FirebaseAuthWeakPasswordException) {
            SIGNUP_STATUS.WEAK_PASSWORD
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            SIGNUP_STATUS.MALFORMED_EMAIL
        } catch (e: Exception) {
            SIGNUP_STATUS.UNKNOWN_ERROR
        }
    }


    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser

}