package com.example.appstory.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appstory.data.model.StoryModel

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(stories: List<StoryModel>)

    @Query("DELETE FROM story")
    fun clearAllStories()

    @Query("SELECT * FROM story ORDER BY id ASC")
    fun getAllStories(): PagingSource<Int, StoryModel>
}
