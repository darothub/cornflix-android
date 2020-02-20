package com.darotapp.cornflix.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.darotapp.cornflix.data.ServiceCall
import com.darotapp.cornflix.data.local.database.*

//class FavouriteRepository( private val databaseDao: FavouriteDao?) :
//    ServiceCall {
//
//
//
////    private val favouriteDao: FavouriteDao = database!!.favouriteDao()
//
//
//    companion object {
//        @Volatile
//        private var INSTANCE: FavouriteRepository? = null
//
//        fun getRepository(context: Context): FavouriteRepository {
//            return INSTANCE ?: synchronized(this) {
//                val database = MovieDatabase.getInstance(context)?.favouriteDao()
//                FavouriteRepository(database).also {
//                    INSTANCE = it
//                }
//            }
//        }
//    }
//
//    override suspend fun favouriteMovies(context: Context):LiveData<List<FavouriteMoviesEntity?>?>? {
//        return databaseDao?.allFav
//    }
//
////    fun getFavMovies(context: Context, favouriteMoviesEntity: FavouriteMoviesEntity) {
////        CoroutineTaskSingleton.getInstance(Application()).insertFav(favouriteDao, favouriteMoviesEntity)
////    }
//
//
//}