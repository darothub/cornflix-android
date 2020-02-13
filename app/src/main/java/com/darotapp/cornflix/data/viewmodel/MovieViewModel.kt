package com.darotapp.cornflix.data.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.darotapp.cornflix.data.repository.FavouriteRepository
import com.darotapp.cornflix.data.repository.MovieRepository
import com.darotapp.cornflix.model.database.FavouriteMoviesEntity
import com.darotapp.cornflix.model.database.MovieEntity

class MovieViewModel(application: Application): AndroidViewModel(application) {


    private var movieRepository =
        MovieRepository(application)
    private var favouriteRepository =
        FavouriteRepository(application)
    private var allMovies = movieRepository.allMovies

    fun getAllMovies(): LiveData<List<MovieEntity?>?>?{
        return allMovies
    }

    suspend fun loadData(context: Context){
        movieRepository.getMovies(context.applicationContext)
    }
    fun getAllFav(): LiveData<List<FavouriteMoviesEntity?>?>?{
        return favouriteRepository.allFav
    }
}