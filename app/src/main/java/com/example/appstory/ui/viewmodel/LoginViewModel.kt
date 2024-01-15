package com.example.appstory.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appstory.data.api.ApiService
import com.example.appstory.data.response.LoginResponse
import com.example.appstory.utils.SharedPreferencesHelper
import com.example.appstory.utils.SharedPreferencesHelper.Companion.KEY_LOGIN_STATUS

class LoginViewModel : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResponse>()
    val loginResult: LiveData<LoginResponse> = _loginResult

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun login(email: String, password: String) {
        val apiService = ApiService
        apiService.login(email, password, { loginResponse ->
            _loginResult.value = loginResponse
        }, { error ->
            _error.value = error
        })
    }

    fun saveLoginStatus(sharedPreferencesHelper: SharedPreferencesHelper, token: String) {
        sharedPreferencesHelper.saveBoolean(KEY_LOGIN_STATUS, true)
        sharedPreferencesHelper.saveString("token", token)
    }
}
