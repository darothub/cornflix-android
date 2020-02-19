package com.darotapp.cornflix

import android.app.Application
import android.util.Log
import com.darotapp.cornflix.data.local.database.MovieDatabase
import com.darotapp.cornflix.data.repository.MoviesRepoInterface

class MovieApplication:Application() {
    val moviesRepoInterface:MoviesRepoInterface
        get() = ServiceLocator.provideMoviesRepository(this)




    override fun onCreate() {
        super.onCreate()
        Log.i("Movieapp", "MovieApplication called")
    }
}

