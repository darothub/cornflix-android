package com.darotapp.cornflix.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.darotapp.cornflix.data.Result
import com.darotapp.cornflix.data.ServiceCall
import com.darotapp.cornflix.data.local.database.FavouriteMoviesEntity
import com.darotapp.cornflix.data.local.database.LocalDataSourceManager
import com.darotapp.cornflix.data.local.database.MovieEntity
import com.darotapp.cornflix.data.remote.RemoteDataSourceManager

class MovieRepository(
    private val remoteDataSourceManager: ServiceCall,
    private val localDataSourceManager: ServiceCall
) : MoviesRepoInterface {
    override suspend fun getMovies(
        fetch: Boolean,
        context: Context,
        page:Int?
    ):LiveData<List<MovieEntity>> {
        var responseList:LiveData<List<MovieEntity>>
        if(fetch){
            responseList = page?.let { remoteDataSourceManager.getRemoteMovies(context, it) }!!
            Log.i("MovieRepo", "remote")
            return responseList
        }
        else{

            responseList = localDataSourceManager.getLocaleMovies(context)!!
//            val size = result.value?.size
//            Result.Success(responseList)
            Log.i("MovieRepo", "local")
            return responseList
        }


    }

    override suspend fun getFavMovies(context: Context): LiveData<List<MovieEntity>>? {
        val result = localDataSourceManager.getFavouriteMovies(context)
        Result.Success(result)
        return result
    }

    override fun getMoviesList(context: Context): List<MovieEntity> {
        val result = localDataSourceManager.getMovieList(context)
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