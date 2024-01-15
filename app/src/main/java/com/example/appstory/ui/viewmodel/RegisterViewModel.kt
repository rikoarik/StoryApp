package com.example.appstory.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appstory.data.api.ApiService
import com.example.appstory.data.model.UserBodyModel
import com.example.appstory.data.response.RegisterResponse
import com.example.appstory.utils.SharedPreferencesHelper

class RegisterViewModel : ViewModel() {

    private val _registerResult = MutableLiveData<RegisterResponse>()
    val registerResult: LiveData<RegisterResponse> = _registerResult

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun register(userBodyModel: UserBodyModel) {
        val apiService = ApiService
        apiService.register(userBodyModel,
            { registerResponse ->
                _registerResult.value = registerResponse
            },
            { error ->
                _error.value = error
            }
        )
    }
}
