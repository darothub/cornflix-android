package com.darotapp.cornflix.data.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.darotapp.cornflix.data.Result
import com.darotapp.cornflix.data.local.database.FavouriteMoviesEntity
import com.darotapp.cornflix.data.local.database.MovieEntity
import com.darotapp.cornflix.data.repository.MoviesRepoInterface

class MovieViewModel(private val moviesRepoInterface: MoviesRepoInterface): ViewModel() {


    var fireModel = MutableLiveData<Boolean>(false)

    suspend fun loadMovies(context: Context, page: Int){
        moviesRepoInterface.getMovies(true, context, page)
    }

    suspend fun getAllMovies(context: Context):LiveData<List<MovieEntity>>{
        fireModel.value = true
        var localData:LiveData<List<MovieEntity>> = moviesRepoInterface.getMovies(false, context)

        return localData
        Log.i("viewmodel", "called")
    }


    suspend fun getAllFavMovies(context: Context): LiveData<List<MovieEntity>>? {
        val result = moviesRepoInterface.getFavMovies(context)
        Result.Success(result)
        return result
    }

    suspend fun getMoviesList(context: Context):List<MovieEntity>{
        val result = moviesRepoInterface.getMoviesList(context)
        Result.Success(result)
        return result
    }
}