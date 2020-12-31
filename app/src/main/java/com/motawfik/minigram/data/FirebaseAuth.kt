package com.motawfik.minigram.data

import android.util.Log
import com.google.firebase.auth.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.motawfik.minigram.models.NewUser
import kotlinx.coroutines.tasks.await

enum class LOGIN_STATUS { NONE, SUCCESS, NO_USER, INVALID_CREDENTIALS, UNKNOWN_ERROR };
enum class SIGNUP_STATUS { NONE, SUCCESS, DUPLICATE_EMAIL, MALFORMED_EMAIL, WEAK_PASSWORD, UNKNOWN_ERROR };

class FirebaseAuth {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val firestore: FirebaseFirestore by lazy {
        Firebase.firestore
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

    suspend fun registerWithEmailAndPassword(user: NewUser): SIGNUP_STATUS {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(user.email, user.password).await()
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                firestore.collection("users")
                    .document(currentUser.uid)
                    .set(user.addToFirestore())
            }
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