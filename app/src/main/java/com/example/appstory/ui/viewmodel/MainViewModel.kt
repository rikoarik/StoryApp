package com.example.appstory.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.appstory.data.model.StoryModel
import com.example.appstory.data.repository.StoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel(private val repository: StoryRepository) : ViewModel() {

    private var currentToken: String = ""
    private var currentStoryList: Flow<PagingData<StoryModel>>? = null

    fun getStories(token: String): Flow<PagingData<StoryModel>> {
        val newToken = token.trim()
        if (currentToken != newToken || currentStoryList == null) {
            currentToken = newToken
            val newStoryList = repository.getStories(newToken).cachedIn(viewModelScope)
            currentStoryList = newStoryList
        }

        currentStoryList?.onEach { pagingData ->
            pagingData.map { data ->
                val items = data.toString() // Ubah ini sesuai dengan representasi yang Anda inginkan
                Log.d("MainViewModel", "Data fetched: $items")
            }
        }?.launchIn(viewModelScope)
        return currentStoryList!!
    }
}
