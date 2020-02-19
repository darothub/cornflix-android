package com.darotapp.cornflix.data.viewmodel.source

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.darotapp.cornflix.data.ServiceCall
import com.darotapp.cornflix.data.local.database.FavouriteMoviesEntity
import com.darotapp.cornflix.data.local.database.MovieEntity

class FakeDataSource(var movies: MutableList<MovieEntity>? = mutableListOf()):
    ServiceCall {
    override suspend fun getMovies(context: Context) {
       val resp = getDataFromRemote(context)

    }

    override suspend fun getDataFromRemote(context: Context):LiveData<Boolean> {
        val responseBool = MutableLiveData<Boolean>(false)
        movies?.let {
            responseBool.postValue(true)
        }
        return responseBool
    }

    override suspend fun favouriteMovies(context: Context): LiveData<List<FavouriteMoviesEntity?>?>? {
        return super.favouriteMovies(context)
    }
}