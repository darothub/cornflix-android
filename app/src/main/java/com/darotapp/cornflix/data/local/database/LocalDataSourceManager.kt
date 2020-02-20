package com.darotapp.cornflix.data.local.database

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.darotapp.cornflix.data.Result.Success
import com.darotapp.cornflix.data.ServiceCall
import com.darotapp.cornflix.ServiceLocator

class LocalDataSourceManager(internal val movieDao: MovieDao?, internal var favouriteDao: FavouriteDao?) : ServiceCall {
    override suspend fun getMovies(context: Context, page: Int?): LiveData<List<MovieEntity>>? {
        val result =ServiceLocator.createLocalDataSource(context).movieDao?.allMovies
//            MovieDatabase.getInstance(context)?.movieDao()?.allMovies
        Success(result)
        Log.i("movies", "$result")
        return result
    }

    override suspend fun getFavouriteMovies(context: Context): LiveData<List<FavouriteMoviesEntity>>? {
        val result = ServiceLocator.createLocalDataSource(context).favouriteDao?.allFav
        Success(result)
        return result
    }


}