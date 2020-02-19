package com.darotapp.cornflix.data.repository

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.room.Room
import com.darotapp.cornflix.data.Result
import com.darotapp.cornflix.data.local.database.FavouriteMoviesEntity
import com.darotapp.cornflix.data.local.database.LocalDataSourceManager
import com.darotapp.cornflix.data.local.database.MovieDatabase
import com.darotapp.cornflix.data.local.database.MovieEntity
import com.darotapp.cornflix.data.network.RemoteDataSourceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class MovieRepository(
    private val remoteDataSourceManager: RemoteDataSourceManager,
    private val localDataSourceManager: LocalDataSourceManager
) : MoviesRepoInterface {
    override suspend fun getMovies(
        forceUpdate: Boolean,
        context: Context
    ):LiveData<List<MovieEntity>> {
        val responseList:LiveData<List<MovieEntity>>
        if(forceUpdate ){
            val result = remoteDataSourceManager.getMovies(context)
            responseList = result
            Log.i("MovieRepo", "remote")
//            result.observeForever {
//                Log.i("MovieRepo", "${it.get(0).title}")
//            }
        }
        else{
            val result = localDataSourceManager.getMovies(context)
            responseList = result
//            val size = result.value?.size
            Result.Success(result)
            Log.i("MovieRepo", "local")
//            localDataSourceManager.getMovies(context)
//            result.observeForever {
//                Log.i("MovieRepo", "${it.get(0).title}")
//            }

        }

        return responseList
    }

    override suspend fun getFavMovies(context: Context): LiveData<List<FavouriteMoviesEntity>> {
        val result = localDataSourceManager.getFavouriteMovies(context)
        Result.Success(result)
        return result
    }

//    private fun fetchRemotely(){
//
//    }

//    companion object {
//        @Volatile
//        private var INSTANCE: MovieRepository? = null
//
//        fun getRepository(app: Application): MovieRepository {
//            return INSTANCE ?: synchronized(this) {
//                val database = Room.databaseBuilder(app,
//                    MovieDatabase::class.java, "MovieDb")
//                    .build()
//                MovieRepository(RemoteDataSourceManager(), LocalDataSourceManager(database.movieDao())).also {
//                    INSTANCE = it
//                }
//            }
//        }
//    }




//    var allMovies = databaseDao?.allMovies



}