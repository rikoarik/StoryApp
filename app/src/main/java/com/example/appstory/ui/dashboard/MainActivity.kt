package com.example.appstory.ui.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appstory.R
import com.example.appstory.data.api.ApiService
import com.example.appstory.data.db.AppDatabase
import com.example.appstory.data.model.StoryModel
import com.example.appstory.data.repository.StoryRepository
import com.example.appstory.ui.adapter.MainActivityAdapter
import com.example.appstory.ui.authenticate.AuthActivity
import com.example.appstory.ui.viewmodel.MainViewModel
import com.example.appstory.ui.viewmodel.MainViewModelFactory
import com.example.appstory.utils.SharedPreferencesHelper
import com.example.appstory.utils.SharedPreferencesHelper.Companion.KEY_LOGIN_STATUS
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), MainActivityAdapter.OnItemClickListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var storyAdapter: MainActivityAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.show()
        supportActionBar?.title = "Story"

        recyclerView = findViewById(R.id.recycleStory)
        progressBar = findViewById(R.id.progressBar)
        sharedPreferencesHelper = SharedPreferencesHelper(this)
        val apiService = ApiService
        val repository = StoryRepository(apiService, database = AppDatabase.getInstance(this))
        val factory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        setupRecyclerView()
        fetchStoryList()

    }
    private fun setupRecyclerView() {
        storyAdapter = MainActivityAdapter(this)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter
        }
    }

    private fun fetchStoryList() {
        val token = sharedPreferencesHelper.getString("token", "")
        lifecycleScope.launch {
            viewModel.getStories(token)
                .collectLatest { storyList ->
                    storyAdapter.submitData(storyList)
                    if (storyList.toString().isEmpty()) {
                        Log.d("MainActivity", "Data is empty")
                    } else {
                        Log.d("MainActivity", "Data fetched: $storyList")
                    }
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.logout -> {
                logout()
            }
            R.id.addStory -> {
                startActivity(Intent(this@MainActivity, AddStoryActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun logout() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { dialog, _ ->
                sharedPreferencesHelper.clearSession()
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
                finishAffinity()
                dialog.dismiss()
                sharedPreferencesHelper.saveBoolean(KEY_LOGIN_STATUS, false)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }

    override fun onItemClick(story: StoryModel) {
        val intent = Intent(this, StoryDetailsActivity::class.java)
        intent.putExtra("id", story.id)
        startActivity(intent)
    }
}
