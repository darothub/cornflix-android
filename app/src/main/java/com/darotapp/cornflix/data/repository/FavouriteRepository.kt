package com.darotapp.cornflix.data.repository

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.darotapp.cornflix.data.network.ServiceCall
import com.darotapp.cornflix.model.database.*
import com.darotapp.cornflix.utils.CoroutineTaskSingleton
import com.darotapp.cornflix.utils.VolleyErrorHandler
import com.darotapp.cornflix.utils.VolleySingleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteRepository( private val databaseDao: FavouriteDao?) : ServiceCall {



//    private val favouriteDao: FavouriteDao = database!!.favouriteDao()
    var allFav = databaseDao?.allFav

    companion object {
        @Volatile
        private var INSTANCE: FavouriteRepository? = null

        fun getRepository(app: Application): FavouriteRepository {
            return INSTANCE ?: synchronized(this) {
                val database = MovieDatabase.getInstance(Application())?.favouriteDao()
                FavouriteRepository(database).also {
                    INSTANCE = it
                }
            }
        }
    }

    override suspend fun getMovies(context: Context) {


    }

//    fun getFavMovies(context: Context, favouriteMoviesEntity: FavouriteMoviesEntity) {
//        CoroutineTaskSingleton.getInstance(Application()).insertFav(favouriteDao, favouriteMoviesEntity)
//    }


}