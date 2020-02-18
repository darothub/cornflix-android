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
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.darotapp.cornflix.R
import com.darotapp.cornflix.adapters.FavouriteMoviesAdapter
import com.darotapp.cornflix.data.viewmodel.MovieViewModel
import com.darotapp.cornflix.model.database.FavouriteMoviesEntity
import com.darotapp.cornflix.model.database.MovieDatabase
import com.darotapp.cornflix.model.database.MovieEntity
import com.google.android.material.snackbar.Snackbar
import com.shashank.sony.fancytoastlib.FancyToast
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

        val nav = Navigation.findNavController(appBar)
        NavigationUI.setupWithNavController(favToolbar, nav)

        val recylerView = view!!.findViewById<RecyclerView>(R.id.recycler_view_fav)



        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        try {
            movieViewModel!!.getAllFav()!!.observeForever {list ->

                if(list.isNullOrEmpty()){
//                    Toast.makeText(context, "You have not added any movie", Toast.LENGTH_SHORT).show()
                    try {
                        placeHolderFav.visibility = View.VISIBLE
                        recylerView.visibility = View.GONE
                        val snackbar = Snackbar
                            .make(appBar, "Favourite list is empty", Snackbar.LENGTH_LONG)
                        snackbar.show()
                    } catch (e: Exception) {
                    }
                }
                else{
                    favMovieAdapter = FavouriteMoviesAdapter(list, object : FavouriteMoviesAdapter.OnMovieListener{
                        override fun onMovieClick(movieEntity: FavouriteMoviesEntity, view: View) {

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

                            CoroutineScope(Dispatchers.Main).launch {
                                MovieDatabase.getInstance(view.context)!!.favouriteDao().delete(movieEntity)
                                MovieDatabase.getInstance(view.context)!!.movieDao().update(updatedMovie)
                            }
//                            findNavController().navigate(R.id.favouriteMoviesFragment)


                            FancyToast.makeText(context,"${movieEntity.title} is removed from favourite", FancyToast.LENGTH_LONG,
                                FancyToast.INFO,true).show()
//                            Toast.makeText(context, "${movieEntity.title} has been removed", Toast.LENGTH_SHORT).show()

                        }

                        override fun onSingleClick(movieEntity: FavouriteMoviesEntity, view: View) {

                            val action = FavouriteMoviesFragmentDirections.actionFavouriteMoviesFragmentToMovieDetailsFragment()
                            action.favMovie = movieEntity
                            Navigation.findNavController(recylerView).navigate(action)
                        }


                    })
                    recylerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                    recylerView.setHasFixedSize(true)
                    recylerView.adapter = favMovieAdapter
                    favMovieAdapter?.setMovie(list)
                }


            }
        } catch (e: Exception) {
        }


    }

}
