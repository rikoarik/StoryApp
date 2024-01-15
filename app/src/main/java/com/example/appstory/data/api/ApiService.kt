package com.example.appstory.data.api

import com.example.appstory.data.model.UserBodyModel
import com.example.appstory.data.response.LoginResponse
import com.example.appstory.data.model.UserModel
import com.example.appstory.data.response.AddStoryResponse
import com.example.appstory.data.response.RegisterResponse
import com.example.appstory.data.response.StoryDetailsResponse
import com.example.appstory.data.response.StoryResponse
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

object ApiService{
    private const val BASE_URL = "https://story-api.dicoding.dev/v1/"

    private val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    private val apiInterface = retrofit.create(ApiInterface::class.java)

    fun login(
        email: String,
        password: String,
        onSuccess: (LoginResponse) -> Unit,
        onError: (String) -> Unit
    ) {

        val call = apiInterface.login(email, password)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        onSuccess(loginResponse)

                    } else {
                        onError("Empty response body")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = if (errorBody.isNullOrEmpty()) {
                        "Unknown error"
                    } else {
                        try {
                            val errorJson = JSONObject(errorBody)
                            errorJson.getString("message")
                        } catch (e: JSONException) {
                            "Unknown error"
                        }
                    }
                    onError(errorMessage)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onError("Request failed: " + t.message)
            }
        })
    }
    fun register(
        userBodyModel: UserBodyModel,
        onSuccess: (RegisterResponse) -> Unit,
        onError: (String) -> Unit
    ) {

        val call = apiInterface.register(userBodyModel)
        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    if (registerResponse != null) {
                        onSuccess(registerResponse)

                    } else {
                        onError("Empty response body")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = if (errorBody.isNullOrEmpty()) {
                        "Unknown error"
                    } else {
                        try {
                            val errorJson = JSONObject(errorBody)
                            errorJson.getString("message")
                        } catch (e: JSONException) {
                            "Unknown error"
                        }
                    }
                    onError(errorMessage)
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                onError("Request failed: " + t.message)
            }
        })
    }
    fun getAllStory(
        token: String,
        page: Int,
        size: Int,
        onSuccess: (StoryResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val call = apiInterface.getAllStory("Bearer $token", page, size)
        call.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful) {
                    val storyResponse = response.body()
                    if (storyResponse != null) {
                        onSuccess(storyResponse)
                    } else {
                        onError(Throwable("Empty response body"))
                    }
                } else {
                    onError(Throwable("API request failed"))
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                onError(t)
            }
        })
    }
    fun getStoryDetails(
        token: String,
        storyId: String,
        onSuccess: (StoryDetailsResponse) -> Unit,
        onError: (String) -> Unit
    ) {

        val apiInterface = retrofit.create(ApiInterface::class.java)
        val call = apiInterface.getStoryDetails("Bearer $token", storyId)
        call.enqueue(object : Callback<StoryDetailsResponse> {
            override fun onResponse(call: Call<StoryDetailsResponse>, response: Response<StoryDetailsResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        onSuccess(responseBody)
                    } else {
                        onError("Empty response body")
                    }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    onError(errorMessage)
                }
            }

            override fun onFailure(call: Call<StoryDetailsResponse>, t: Throwable) {
                onError("Request failed: ${t.message}")
            }
        })
    }

    fun addNewStory(
        token: String,
        description: String,
        photo: MultipartBody.Part,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val authorization = "Bearer $token"
        val requestDescription = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val call = apiInterface.addNewStory(authorization, requestDescription, photo)
        call.enqueue(object : Callback<AddStoryResponse> {
            override fun onResponse(call: Call<AddStoryResponse>, response: Response<AddStoryResponse>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    onError(errorMessage)
                }
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                onError("Request failed: ${t.message}")
            }
        })
    }

}

