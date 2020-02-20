package com.darotapp.cornflix.data.viewmodel.source

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.darotapp.cornflix.data.ServiceCall
import com.darotapp.cornflix.data.local.database.FavouriteMoviesEntity
import com.darotapp.cornflix.data.local.database.MovieEntity

class FakeDataSource <T>(var movies: MutableList<T>?):
    ServiceCall {
    override suspend fun getMovies(context: Context, page: Int?): LiveData<List<MovieEntity>>? {
        val result = MutableLiveData<List<MovieEntity>>()
        result.value = this.movies as MutableList<MovieEntity>

        return result
    }

    override suspend fun getFavouriteMovies(context: Context): LiveData<List<FavouriteMoviesEntity>>? {
        return movies as LiveData<List<FavouriteMoviesEntity>>
    }

}