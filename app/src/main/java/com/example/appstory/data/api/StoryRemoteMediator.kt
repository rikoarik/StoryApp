package com.example.appstory.data.api

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.appstory.data.db.AppDatabase
import com.example.appstory.data.model.StoryModel

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val apiService: ApiService,
    private val database: AppDatabase,
    private val token: String
) : RemoteMediator<Int, StoryModel>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryModel>
    ): MediatorResult {
        try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    val lastPage = lastItem?.id ?: return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                    lastPage + 1
                }
            }

            val pageSize = state.config.pageSize

            apiService.getAllStory(token, page, pageSize,
                onSuccess = { storyResponse ->
                    val stories = storyResponse.listStory
                    if (loadType == LoadType.REFRESH) {
                        database.runInTransaction {
                            database.storyDao().clearAllStories()
                            database.storyDao().insertAll(stories)
                        }
                    } else {
                        database.storyDao().insertAll(stories)
                    }

                    val endOfPaginationReached = stories.isEmpty()
                    MediatorResult.Success(endOfPaginationReached)

                },
                onError = { error ->
                    MediatorResult.Error(error)
                }
            )
            return MediatorResult.Success(endOfPaginationReached = true)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }
}
