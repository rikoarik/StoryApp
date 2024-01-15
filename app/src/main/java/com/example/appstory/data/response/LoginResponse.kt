package com.example.appstory.data.response

import com.example.appstory.data.model.UserModel
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("loginResult")
    val loginResult: UserModel
)