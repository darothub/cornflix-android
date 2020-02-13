package com.darotapp.cornflix.model.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favouriteMoviesEntity: FavouriteMoviesEntity)

    @Update
    suspend fun update(favouriteMoviesEntity: FavouriteMoviesEntity)

    @Delete
    suspend fun delete(favouriteMoviesEntity: FavouriteMoviesEntity)


    @get:Query(
        "SELECT * FROM favouritemoviesentity ORDER BY id DESC"
    )
    val allFav: LiveData<List<FavouriteMoviesEntity?>?>?
}