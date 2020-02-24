package com.darotapp.cornflix.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(movieEntity: MovieEntity)

    @Update
    suspend fun update(movieEntity: MovieEntity)

    @Delete
    suspend fun delete(movieEntity: MovieEntity)

    @Query("SELECT * FROM movieentity")
    suspend fun getMovies(): List<MovieEntity>


    @get:Query(
        "SELECT * FROM movieentity"
    )
    val allMovies: LiveData<List<MovieEntity>>

    @Query("DELETE FROM movieentity")
    suspend fun deleteAllMovies()


    @Query("SELECT * FROM movieentity")
    fun observeMovies(): LiveData<List<MovieEntity>>
}