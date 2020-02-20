package com.darotapp.cornflix.utils

import android.app.Application
import com.darotapp.cornflix.data.local.database.FavouriteDao
import com.darotapp.cornflix.data.local.database.FavouriteMoviesEntity
import com.darotapp.cornflix.data.local.database.MovieDao
import com.darotapp.cornflix.data.local.database.MovieEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CoroutineTaskSingleton (application: Application) {

    companion object {
        @Volatile
        private var INSTANCE: CoroutineTaskSingleton? = null
        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: CoroutineTaskSingleton(application).also {
                    INSTANCE = it
                }
            }
    }



    fun insertTask(movieDao: MovieDao, movieEntity: MovieEntity){

        CoroutineScope(Dispatchers.IO).launch {
            movieDao.insert(movieEntity)
        }
    }

    fun updateTask(movieDao: MovieDao, movieEntity: MovieEntity){
        CoroutineScope(Dispatchers.IO).launch {
            movieDao.update(movieEntity)
        }
    }
    fun deleteTask(movieDao: MovieDao, movieEntity: MovieEntity){
        CoroutineScope(Dispatchers.IO).launch {
            movieDao.delete(movieEntity)
        }
    }

    fun insertFav(favouriteDao: FavouriteDao, favouriteMoviesEntity: FavouriteMoviesEntity){
        CoroutineScope(Dispatchers.IO).launch {
            favouriteDao.insert(favouriteMoviesEntity)
        }
    }

}