package com.example.appstory.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appstory.data.api.ApiService
import com.example.appstory.data.model.StoryModel

class StoryDetailsViewModel : ViewModel() {
    private val _storyDetails = MutableLiveData<StoryModel>()
    val storyDetails: LiveData<StoryModel> get() = _storyDetails

    fun getStoryDetails(token: String, id: String) {
        val apiService = ApiService
        apiService.getStoryDetails(token, id,
            onSuccess = { storyDetailsResponse ->
                _storyDetails.value = storyDetailsResponse.story
            },
            onError = {
                // Handle error
            }
        )
    }
}







