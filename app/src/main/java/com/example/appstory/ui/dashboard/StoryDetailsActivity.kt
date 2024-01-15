package com.example.appstory.ui.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.appstory.R
import com.example.appstory.data.model.StoryModel
import com.example.appstory.ui.viewmodel.StoryDetailsViewModel
import com.example.appstory.utils.SharedPreferencesHelper

class StoryDetailsActivity : AppCompatActivity() {

    private lateinit var imageDetails: ImageView
    private lateinit var nameDetails: TextView
    private lateinit var descDetails: TextView
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var viewModel: StoryDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_details)
        supportActionBar?.title = "Details Story"

        sharedPreferencesHelper = SharedPreferencesHelper(this)

        imageDetails = findViewById(R.id.imageDetails)
        nameDetails = findViewById(R.id.nameDetails)
        descDetails = findViewById(R.id.descDetails)

        viewModel = ViewModelProvider(this).get(StoryDetailsViewModel::class.java)
        viewModel.storyDetails.observe(this, Observer { story ->
            story?.let {
                displayStoryDetails(story)
            }
        })

        getStoryDetails()
    }

    private fun displayStoryDetails(story: StoryModel) {
        nameDetails.text = story.name
        descDetails.text = story.description

        Glide.with(this)
            .load(story.photoUrl)
            .into(imageDetails)
    }

    private fun getStoryDetails() {
        val id = intent.getStringExtra("id").toString()
        val token = sharedPreferencesHelper.getString("token", "")
        viewModel.getStoryDetails(token, id)
    }
}