package com.darotapp.cornflix.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.darotapp.cornflix.data.local.database.FavouriteMoviesEntity
import com.darotapp.cornflix.data.local.database.MovieDatabase
import com.darotapp.cornflix.data.local.database.MovieEntity

interface ServiceCall {

    suspend fun getMovies(context: Context):LiveData<List<MovieEntity>> {
        var go =MutableLiveData<List<MovieEntity>>()
        return go
    }
    suspend fun getFavouriteMovies(context: Context):LiveData<List<FavouriteMoviesEntity>> {
        var go =MutableLiveData<List<FavouriteMoviesEntity>>()
        return go
    }
}