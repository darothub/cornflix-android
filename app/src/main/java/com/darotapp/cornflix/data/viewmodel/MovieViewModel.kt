package com.darotapp.cornflix.data.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.darotapp.cornflix.data.repository.FavouriteRepository
import com.darotapp.cornflix.data.repository.MovieRepository
import com.darotapp.cornflix.model.database.FavouriteMoviesEntity
import com.darotapp.cornflix.model.database.MovieEntity
import kotlinx.coroutines.launch

class MovieViewModel(application: Application): AndroidViewModel(application) {


    private var movieRepository =
        MovieRepository(application)
    private var favouriteRepository =
        FavouriteRepository.getRepository(application)
    private var allMovies = movieRepository.allMovies

    var fireModel = MutableLiveData<Boolean>(false)

    fun getAllMovies(): LiveData<List<MovieEntity?>?>?{
        fireModel.value = true
        return allMovies
    }

    suspend fun loadData(context: Context){
        movieRepository.getMovies(context.applicationContext)
    }
    fun getAllFav(): LiveData<List<FavouriteMoviesEntity?>?>?{
//        viewModelScope.launch {  }
        return favouriteRepository.allFav
    }
}