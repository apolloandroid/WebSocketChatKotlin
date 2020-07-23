package com.example.websocketchatkotlin.ui.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class LoginViewModel private constructor(
    application: Application
) : ViewModel() {

    private val _loginStatus = MutableLiveData<LoginStatus>()
    val loginStatus: LiveData<LoginStatus>
        get() = _loginStatus

    companion object {
        fun getInstance(application: Application): LoginViewModel {
            var instance: LoginViewModel? = null
            if (instance == null) instance = LoginViewModel(application)
            return instance
        }
    }

    fun onButtonEnterClickListener(userName: String) {
        if (userName.isNotEmpty()) {
            _loginStatus.value = LoginStatus.SUCCESS
        }
        else _loginStatus.value = LoginStatus.EMPTY_USERNAME
    }
}