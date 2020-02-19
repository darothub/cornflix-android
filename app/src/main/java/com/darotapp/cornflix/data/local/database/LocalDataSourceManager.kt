package com.darotapp.cornflix.data.local.database

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.darotapp.cornflix.data.Result
import com.darotapp.cornflix.data.Result.Success
import com.darotapp.cornflix.data.ServiceCall
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import androidx.lifecycle.map
import com.darotapp.cornflix.MovieApplication
import com.darotapp.cornflix.ServiceLocator

class LocalDataSourceManager(private val movieDao: MovieDao) : ServiceCall {
    override suspend fun getMovies(context: Context):LiveData<List<MovieEntity>> {
        val result = ServiceLocator.createDataBase(context).movieDao().allMovies
        Success(result)
        Log.i("movies", "$result")
        return result
    }

    override suspend fun getFavouriteMovies(context: Context):LiveData<List<FavouriteMoviesEntity>> {
        val result = ServiceLocator.createDataBase(context).favouriteDao().allFav
        Success(result)
        return result
    }


}