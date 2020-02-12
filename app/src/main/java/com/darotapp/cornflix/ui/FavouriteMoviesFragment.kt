package com.darotapp.cornflix.ui


import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.darotapp.cornflix.R
import com.darotapp.cornflix.adapters.FavouriteMoviesAdapter
import com.darotapp.cornflix.data.viewmodel.MovieViewModel
import com.darotapp.cornflix.model.database.FavouriteMoviesEntity
import com.darotapp.cornflix.model.database.MovieDatabase
import com.darotapp.cornflix.model.database.MovieEntity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_favourite_movies.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 */
class FavouriteMoviesFragment : Fragment() {

    var movieViewModel: MovieViewModel?=null
    var favMovieAdapter:FavouriteMoviesAdapter?=null
    var newView:View? = null
    var i = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite_movies, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val recylerView = view!!.findViewById<RecyclerView>(R.id.recycler_view_fav)
        recylerView.layoutManager = GridLayoutManager(context, 2)
        recylerView.setHasFixedSize(true)


        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        try {
            movieViewModel!!.getAllFav()!!.observeForever {list ->

                if(list.isNullOrEmpty()){
//                    Toast.makeText(context, "You have not added any movie", Toast.LENGTH_SHORT).show()
                    val snackbar = Snackbar
                        .make(recylerView, "Favourite list is empty", Snackbar.LENGTH_LONG)
                    snackbar.show()
                }
                else{
                    favMovieAdapter = FavouriteMoviesAdapter(list, object : FavouriteMoviesAdapter.OnMovieListener{
                        override fun onMovieClick(movieEntity: FavouriteMoviesEntity, view: View) {

                            i=i.plus(1)
                            val handler = Handler()
                            val runn = Runnable {
                                i = 0
                            }
                            if(i == 1){
                                Toast.makeText(context, "Single Clicked ", Toast.LENGTH_SHORT).show()
                                handler.postDelayed(runn, 400)
                            }
                            else if(i == 2){
                                val fav = view.findViewById<ImageView>(R.id.redFav)
                                fav.visibility = View.GONE
                                movieEntity.favourite = false
                                val(title, movieImage, rating, overView, releaseDate ) = movieEntity

                                val updatedMovie = MovieEntity(
                                    title,
                                    movieImage,
                                    rating,
                                    overView,
                                    releaseDate
                                )
                                updatedMovie.favourite = movieEntity.favourite
                                updatedMovie.movieId = movieEntity.movieId
                                updatedMovie.id = movieEntity.id

                                CoroutineScope(Dispatchers.IO).launch {
                                    MovieDatabase.getInstance(view.context)!!.favouriteDao().delete(movieEntity)
                                    MovieDatabase.getInstance(view.context)!!.movieDao().update(updatedMovie)
                                }
                                activity?.recreate()


//                                val(title, movieImage, rating, overView, releaseDate ) = movieEntity
//                                val favMovie = FavouriteMoviesEntity(
//                                    title,
//                                    movieImage,
//                                    rating,
//                                    overView,
//                                    releaseDate
//                                )
//                                favMovie.favourite = movieEntity.favourite
//                                favMovie.movieId = movieEntity.movieId


                                Toast.makeText(context, "${movieEntity.title} has been removed", Toast.LENGTH_SHORT).show()

                            }
                            return
                        }


                    })
                    recylerView.adapter = favMovieAdapter
                    favMovieAdapter?.setMovie(list)
                }


            }
        } catch (e: Exception) {
        }
    }

}
