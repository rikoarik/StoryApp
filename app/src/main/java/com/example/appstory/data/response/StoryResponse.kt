package com.example.appstory.data.response

import com.example.appstory.data.model.StoryModel
import com.google.gson.annotations.SerializedName

data class StoryResponse(
    val error: Boolean,
    val message: String,

    @field:SerializedName("listStory")
    val listStory: List<StoryModel>
)
