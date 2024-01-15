package com.example.appstory.data.response

import com.example.appstory.data.model.StoryModel

data class StoryDetailsResponse(
    val error: String,
    val message: String,
    val story: StoryModel
)