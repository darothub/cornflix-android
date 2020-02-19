package com.darotapp.cornflix.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.darotapp.cornflix.data.Result
import com.darotapp.cornflix.data.local.database.FavouriteMoviesEntity
import com.darotapp.cornflix.data.local.database.MovieEntity

interface MoviesRepoInterface {
    suspend fun getMovies(forceUpdate: Boolean = false, context: Context):LiveData<List<MovieEntity>>

    suspend fun getFavMovies(context: Context):LiveData<List<FavouriteMoviesEntity>>

//    fun observeMovies(context: Context): LiveData<Result<List<MovieEntity>>>

}
