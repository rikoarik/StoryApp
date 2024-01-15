package com.example.appstory.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.appstory.data.api.ApiService
import com.example.appstory.data.api.StoryRemoteMediator
import com.example.appstory.data.db.AppDatabase
import com.example.appstory.data.model.StoryModel
import kotlinx.coroutines.flow.Flow

class StoryRepository(
    private val apiService: ApiService,
    private val database: AppDatabase
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getStories(token: String): Flow<PagingData<StoryModel>> {
        val pagingConfig = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false
        )

        val remoteMediator = StoryRemoteMediator(apiService, database, token)
        return Pager(
            config = pagingConfig,
            remoteMediator = remoteMediator,
            pagingSourceFactory = { database.storyDao().getAllStories() }
        ).flow
    }
}
