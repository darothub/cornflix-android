package com.darotapp.cornflix

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.darotapp.cornflix.data.local.database.LocalDataSourceManager
import com.darotapp.cornflix.data.local.database.MovieDatabase
import com.darotapp.cornflix.data.network.RemoteDataSourceManager
import com.darotapp.cornflix.data.repository.MovieRepository
import com.darotapp.cornflix.data.repository.MoviesRepoInterface

object ServiceLocator {

    private var database: MovieDatabase? = null
    @Volatile
    var moviesRepoInterface: MoviesRepoInterface? = null
        @VisibleForTesting set

    private val lock = Any()

    fun provideMoviesRepository(context: Context): MoviesRepoInterface {
        synchronized(this) {
            return moviesRepoInterface ?: createMoveRepository(context)
        }

    }

    private fun createMoveRepository(context: Context): MoviesRepoInterface {
        val newRepo = MovieRepository(RemoteDataSourceManager(), createLocalDataSource(context))
        moviesRepoInterface = newRepo
        return newRepo
    }

    private fun createLocalDataSource(context: Context): LocalDataSourceManager {
        val database = database ?: createDataBase(context)
        return LocalDataSourceManager(database.movieDao())
    }

    fun createDataBase(context: Context): MovieDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            MovieDatabase::class.java, "MovieDb"
        )
            .fallbackToDestructiveMigration()
            .build()
        database = result
        return result
    }



}