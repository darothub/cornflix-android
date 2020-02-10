package com.darotapp.cornflix.model.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDao {

    @Insert
    suspend fun insert(movieEntity: MovieEntity)

    @Update
    suspend fun update(movieEntity: MovieEntity)

    @Delete
    suspend fun delete(movieEntity: MovieEntity)


    @get:Query(
        "SELECT * FROM movieentity ORDER BY id ASC"
    )
    val allPosts: LiveData<List<MovieEntity?>?>?
}