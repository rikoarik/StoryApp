package com.example.appstory.data.api


import com.example.appstory.data.model.UserBodyModel
import com.example.appstory.data.response.LoginResponse
import com.example.appstory.data.model.UserModel
import com.example.appstory.data.response.AddStoryResponse
import com.example.appstory.data.response.RegisterResponse
import com.example.appstory.data.response.StoryDetailsResponse
import com.example.appstory.data.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @POST("register")
    fun register(
        @Body userBodyModel: UserBodyModel
    ): Call<RegisterResponse>

    @GET("stories")
    fun getAllStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ):Call<StoryResponse>

    @Multipart
    @POST("stories")
    fun addNewStory(
        @Header("Authorization") authorization: String,
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part
    ): Call<AddStoryResponse>

    @GET("stories/{id}")
    fun getStoryDetails(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ):Call<StoryDetailsResponse>

}