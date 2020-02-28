package com.darotapp.cornflix.data.remote

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.darotapp.cornflix.ServiceLocator
import com.darotapp.cornflix.data.Result
import com.darotapp.cornflix.data.ServiceCall
import com.darotapp.cornflix.data.local.database.FavouriteMoviesEntity
import com.darotapp.cornflix.data.local.database.MovieDatabase
import com.darotapp.cornflix.data.local.database.MovieEntity
import com.darotapp.cornflix.utils.VolleyErrorHandler
import com.darotapp.cornflix.utils.VolleySingleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RemoteDataSourceManager : ServiceCall {
    override suspend fun getRemoteMovies(context: Context, page: Int?):LiveData<List<MovieEntity>> {
        return getMovies(context, page)
    }





    private fun getMovies(context: Context, page: Int?): LiveData<List<MovieEntity>> {
        val responseList = MutableLiveData<List<MovieEntity>>()
        var listResult: Result<List<MovieEntity>>? = null
        val listOfMovies = ArrayList<MovieEntity>()

        val url =
            "https://api.themoviedb.org/3/movie/popular?api_key=f1e256985ebc2be710bf1f4ed754da11&language=en-US&page=${page?:1}"
        val request: JsonObjectRequest =
            JsonObjectRequest(
                Request.Method.GET,
                url, null,
                Response.Listener { response ->

                    try {
                        val results = response.getJSONArray("results")
                        val size = results.length()
                        var title = ""
                        var movieImage = ""
                        var rating = 0
                        var overView = ""
                        var releaseDate = ""
                        var movieId = ""

                        for (movie in 0 until results.length()) {
                            val eachMovie = results.getJSONObject(movie)
                            movieImage = "https://image.tmdb.org/t/p/w500"
                            title = eachMovie.getString("title")
                            movieImage += eachMovie.getString("poster_path")
                            rating = eachMovie.getInt("vote_average")
                            overView = eachMovie.getString("overview")
                            releaseDate = eachMovie.getString("release_date")
                            movieId = eachMovie.getInt("id").toString()

                            val newMovie = MovieEntity(
                                title,
                                movieImage,
                                rating,
                                overView,
                                releaseDate
                            )
                            newMovie.movieId = movieId
                            newMovie.id = movieId.toInt()
                            listOfMovies.add(newMovie)

                            GlobalScope.launch {
                                ServiceLocator.createLocalDataSource(context).movieDao?.insert(newMovie)
//                                MovieDatabase.getInstance(context)?.movieDao()?.insert(newMovie)
                            }


                        }
                        responseList.value = listOfMovies.toList()

                        Log.i("insideList", "$listOfMovies")

                    } catch (e: Exception) {
                        Log.i("error", "Response $e")

//                        Toast.makeText(context!!.applicationContext, "No activity", Toast.LENGTH_SHORT).show()
                    }
                }, Response.ErrorListener { error ->

                    val errroMessage = VolleyErrorHandler.instance.erroHandler(error)
                    CoroutineScope(Dispatchers.Main).launch {
                        //                        Toast.makeText(context, errroMessage, Toast.LENGTH_LONG).show()
                    }

                })

        context.applicationContext.let {
            VolleySingleton.getInstance(it).addToRequestQueue(request)

        }

        Log.i("listOfMovies", "${responseList.value}")
        return responseList
    }




}