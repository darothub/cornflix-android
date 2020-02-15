package com.darotapp.cornflix.data.repository

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.darotapp.cornflix.data.network.ServiceCall
import com.darotapp.cornflix.model.database.MovieDao
import com.darotapp.cornflix.model.database.MovieDatabase
import com.darotapp.cornflix.model.database.MovieEntity
import com.darotapp.cornflix.utils.CoroutineTaskSingleton
import com.darotapp.cornflix.utils.VolleyErrorHandler
import com.darotapp.cornflix.utils.VolleySingleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MovieRepository(application: Application) : ServiceCall {

    private val database = MovieDatabase.getInstance(application)

    private val movieDao: MovieDao = database!!.movieDao()
    var allMovies = movieDao.allMovies

    override suspend fun getMovies(context: Context) {
        loadData(context)

    }

    private fun loadData(context: Context) {

        val url =
            "https://api.themoviedb.org/3/movie/popular?api_key=f1e256985ebc2be710bf1f4ed754da11&language=en-US&page=1"


        val request: JsonObjectRequest =
        JsonObjectRequest(
            Request.Method.GET,
            url, null,
            Response.Listener { response ->

                try {

//
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
                        movieImage +=eachMovie.getString("poster_path")
                        rating  = eachMovie.getInt("vote_average")
                        overView = eachMovie.getString("overview")
                        releaseDate = eachMovie.getString("release_date")
                        movieId = eachMovie.getInt("id").toString()

                        val newMovie =  MovieEntity(
                            title,
                            movieImage,
                            rating,
                            overView,
                            releaseDate
                        )
                        newMovie.movieId = movieId
                        newMovie.id = movieId.toInt()
                        CoroutineScope(Dispatchers.IO).launch {

                            try {
                                MovieDatabase.getInstance(
                                    context
                                )?.movieDao()?.insert(
                                    newMovie
                                )
                            } catch (e: Exception) {
                                runBlocking {
                                    Toast.makeText(context.applicationContext, "updating existing data", Toast.LENGTH_SHORT).show()
                                }

                            }
                        }


                        CoroutineScope(Dispatchers.Main).launch {
                            Log.i("response", "Response ${newMovie.movieImage}")
                        }
                    }
//






                } catch (e: Exception) {
                    Log.i("error", "Response $e")

                    Toast.makeText(context!!.applicationContext, "No activity", Toast.LENGTH_SHORT).show()
                }

            }, Response.ErrorListener { error ->


                val errroMessage = VolleyErrorHandler.instance.erroHandler(error, context)
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, errroMessage, Toast.LENGTH_LONG).show()
                }


            })
        context.applicationContext.let {
            VolleySingleton.getInstance(it).addToRequestQueue(request)

        }


    }


}