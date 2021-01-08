package com.motawfik.minigram.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.facebook.AccessToken
import com.facebook.Profile
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.motawfik.minigram.signup.NewUser
import kotlinx.coroutines.tasks.await

enum class LOGIN_STATUS { NONE, SUCCESS, NO_USER, INVALID_CREDENTIALS, UNKNOWN_ERROR };
enum class FACEBOOK_LOGIN_STATUS { NONE, SUCCESS, DUPLICATE_EMAIL, UNKNOWN_ERROR };
enum class SIGNUP_STATUS { NONE, SUCCESS, DUPLICATE_EMAIL, MALFORMED_EMAIL, WEAK_PASSWORD, UNKNOWN_ERROR };

class FirebaseAuth {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val firestore: FirebaseFirestore by lazy {
        Firebase.firestore
    }

    init {
        firebaseAuth.addAuthStateListener {
            _currentUser.value = it.currentUser
        }
    }
    private val _currentUser = MutableLiveData<FirebaseUser?>()
    val currentUser: LiveData<FirebaseUser?>
        get() = _currentUser

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

    suspend fun loginWithGoogle(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                firebaseAuth.signInWithCredential(credential).await()
                val currentUser = firebaseAuth.currentUser
                if (currentUser != null) {
                    firestore.collection("users")
                        .document(currentUser.uid)
                        .set(
                            hashMapOf(
                                "name" to account.displayName
                            )
                        )
                }
            }
        } catch (e: ApiException) {
            Log.d("GOOGLE_LOGIN", e.toString())
            Log.d("GOOGLE_LOGIN", e.printStackTrace().toString())
        }
    }

    suspend fun loginWithFacebook(token: AccessToken): FACEBOOK_LOGIN_STATUS {
        val credential = FacebookAuthProvider.getCredential(token.token)

        return try {
            firebaseAuth.signInWithCredential(credential).await()
            val currentUser = firebaseAuth.currentUser
            return if (currentUser != null) {
                val profile = Profile.getCurrentProfile()
                firestore.collection("users")
                    .document(currentUser.uid)
                    .set(hashMapOf("name" to profile.name))
                FACEBOOK_LOGIN_STATUS.SUCCESS
            } else {
                FACEBOOK_LOGIN_STATUS.UNKNOWN_ERROR
            }
        } catch (e: FirebaseAuthUserCollisionException) {
            FACEBOOK_LOGIN_STATUS.DUPLICATE_EMAIL
        } catch (e: Exception) {
            FACEBOOK_LOGIN_STATUS.UNKNOWN_ERROR
        }
    }

    fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun logout() = firebaseAuth.signOut()

    fun currentUserID() = firebaseAuth.currentUser?.uid

}