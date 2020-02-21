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


//    private var movieRepository =
//        MovieRepository.getRepository(application)
//    private var favouriteRepository =
//        FavouriteRepository.getRepository(application)
//    private var allMovies = movieRepository.allMovies

    var fireModel = MutableLiveData<Boolean>(false)

    suspend fun loadMovies(context: Context, page: Int){
        moviesRepoInterface.getMovies(true, context, page)
    }

    suspend fun getAllMovies(context: Context):LiveData<List<MovieEntity>>{
        fireModel.value = true
        var localData:LiveData<List<MovieEntity>> = moviesRepoInterface.getMovies(false, context)

//        if(localData.value?.get(0)?.movieId.isNullOrEmpty()){
//            Log.i("viewmodelLRemoteBfr", "${localData.value?.get(0)?.movieId}")
//            localData = moviesRepoInterface.getMovies(true, context)
//            Log.i("viewmodelRemoteAfter", "${localData.value?.get(0)?.movieId}")
//        }
//        else{
//            Log.i("viewmodelLocaldataBf", "${localData.value?.get(0)?.movieId}")
//            localData = moviesRepoInterface.getMovies(false, context)
//            Log.i("viewmodelLocaldataAf", "${localData.value?.get(0)?.movieId}")
//        }
        return localData
        Log.i("viewmodel", "called")
    }


    suspend fun getAllFavMovies(context: Context): LiveData<List<MovieEntity>>? {
        val result = moviesRepoInterface.getFavMovies(context)
        Result.Success(result)
        return result
    }

//    fun observeAllMovies(context: Context): LiveData<Result<List<MovieEntity>>>{
//        return moviesRepoInterface.observeMovies(context)
//    }
//    suspend fun loadData(context: Context){
//        movieRepository.getMovies(context.applicationContext)
//    }
//    suspend fun getAllFav(context: Context): LiveData<List<FavouriteMoviesEntity?>?>?{
////        viewModelScope.launch {  }
//        return favouriteRepository.favouriteMovies(context)
//    }
}