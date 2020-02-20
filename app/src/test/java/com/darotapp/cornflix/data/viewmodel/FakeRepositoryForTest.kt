package com.darotapp.cornflix.data.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.darotapp.cornflix.data.ServiceCall
import com.darotapp.cornflix.data.local.database.FavouriteMoviesEntity
import com.darotapp.cornflix.data.local.database.MovieEntity
import com.darotapp.cornflix.data.repository.MoviesRepoInterface

class FakeRepositoryForTest(
    private var remoteDataSource: ServiceCall,
    private var localDataSource:ServiceCall
):MoviesRepoInterface {
    override suspend fun getMovies(
        fetch: Boolean,
        context: Context,
        page: Int?
    ): LiveData<List<MovieEntity>> {
        var responseList:LiveData<List<MovieEntity>>
        if(fetch){
//            val movie3 = MovieEntity("Title3", "https://movie3.jpg", 3, "Movie2Overview", "Jan 2023")
//            val remoteTasks = mutableListOf(movie3).sortedBy { it.id }

//            responseList = page?.let { remoteDataSource.getMovies(context, null) }!!

            return remoteDataSource.getMovies(context, null)!!

//            Result.Success(responseList)
            Log.i("MovieRepo", "remote")
        }
        else{

            responseList = localDataSource.getMovies(context, page)!!
            Log.i("MovieRepo", "local")


        }

        return responseList
    }

    override suspend fun getFavMovies(context: Context): LiveData<List<FavouriteMoviesEntity>>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}