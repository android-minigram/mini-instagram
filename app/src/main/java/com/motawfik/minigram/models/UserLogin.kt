package com.motawfik.minigram.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.motawfik.minigram.BR


class UserLogin : BaseObservable() {
    var email: String? = null
    @Bindable get
        set(username) {
        field=username
        notifyPropertyChanged(BR.email)
    }

    var password: String? = null
    @Bindable get
        set(password) {
        field = password
        notifyPropertyChanged(BR.password)
    }
}