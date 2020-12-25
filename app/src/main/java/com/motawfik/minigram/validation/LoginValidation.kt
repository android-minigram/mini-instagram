package com.motawfik.minigram.validation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.regex.Pattern

class LoginValidation() { // equivalent to the REPO which will fetch the data

    fun validateCredentials(emailID:String,password:String): LiveData<String> {
        val loginErrorMessage = MutableLiveData<String>()
        if(isEmailValid(emailID)){
            if(password.length<8 && !isPasswordValid(password)){
                loginErrorMessage.value = "Invalid Password"
            }else{
                loginErrorMessage.value = "Successful Login"
            }
        }else{
            loginErrorMessage.value = "Invalid Email"
        }
        return  loginErrorMessage
    }





    private fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun isPasswordValid(password: String): Boolean{
        val expression  ="^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#\$%^&+=!])(?=\\\\S+\$).{4,}\$";
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }



}