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
import kotlin.properties.Delegates

class MovieViewModel(private val moviesRepoInterface: MoviesRepoInterface): ViewModel() {


    var fireModel = MutableLiveData<Boolean>(false)
    var listSize = 0


    suspend fun loadMovies(context: Context, page: Int){
        moviesRepoInterface.getMovies(true, context, page)
    }

    suspend fun getAllMovies(fetch:Boolean=false, context: Context):LiveData<List<MovieEntity>>{
        fireModel.value = true
        var localData:LiveData<List<MovieEntity>> = moviesRepoInterface.getMovies(fetch, context)

        getMoviesList(context)
        return localData
        Log.i("viewmodel", "called")
    }


    suspend fun getAllFavMovies(context: Context): LiveData<List<MovieEntity>>? {
        val result = moviesRepoInterface.getFavMovies(context)
        Result.Success(result)
        return result
    }

    fun getMoviesList(context: Context):List<MovieEntity>{
        val result = moviesRepoInterface.getMoviesList(context)
        listSize = result.size
        Result.Success(result)
        return result
    }

    fun getMoviesSize(context: Context):Int{
        return getMoviesList(context).size
    }
}