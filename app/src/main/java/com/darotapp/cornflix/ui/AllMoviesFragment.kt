package com.darotapp.cornflix.ui


import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.darotapp.cornflix.R
import com.darotapp.cornflix.adapters.MovieAdapter
import com.darotapp.cornflix.adapters.SwipeAdapter
import com.darotapp.cornflix.data.viewmodel.MovieViewModel
import com.darotapp.cornflix.model.database.FavouriteMoviesEntity
import com.darotapp.cornflix.model.database.MovieDatabase
import com.darotapp.cornflix.model.database.MovieEntity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_all_movies.*
import kotlinx.android.synthetic.main.fragment_favourite_movies.*
import kotlinx.android.synthetic.main.fragment_landing.*
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class AllMoviesFragment : Fragment() {
    var movieViewModel: MovieViewModel?=null
    var movieAdapter:MovieAdapter?=null
    var i = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_all_movies, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        //Attaching navigation to the app bar and toolbar
        val nav = Navigation.findNavController(post_appbar)
        NavigationUI.setupWithNavController(allMoviesToolbar, nav)

//        Toast.makeText(context, "onActivity created", Toast.LENGTH_SHORT).show()
        val recylerView = view!!.findViewById<RecyclerView>(R.id.recycler_view_movies)
        recylerView.layoutManager = GridLayoutManager(context, 2)
        recylerView.setHasFixedSize(true)
        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        CoroutineScope(Dispatchers.Main).launch {
            movieViewModel!!.loadData(context!!)
//            MovieDatabase.getInstance(context!!)?.movieDao()!!.insert(MovieEntity("hello", "hi",4, "helllo"))
        }

        try {

            movieViewModel!!.getAllMovies()!!.observeForever { list ->
                //            Log.i("listi", "${list?.last()?.title}")



                movieAdapter = MovieAdapter(list, object :MovieAdapter.OnMovieListener{
                    override fun onMovieClick(movieEntity: MovieEntity, view: View) {

                        val fav = view.findViewById<ImageView>(R.id.redFav)
                        var favMovie:FavouriteMoviesEntity? = null
                        val(title, movieImage, rating, overView, releaseDate ) = movieEntity
                        favMovie = FavouriteMoviesEntity(
                            title,
                            movieImage,
                            rating,
                            overView,
                            releaseDate
                        )

                        favMovie.movieId = movieEntity.movieId
                        favMovie.id = movieEntity.id
                        if(fav.visibility == View.GONE){
                            fav.visibility = View.VISIBLE
                            movieEntity.favourite = true
                            favMovie.favourite = movieEntity.favourite



                            CoroutineScope(Main).launch {

                                try {
                                    MovieDatabase.getInstance(view.context)!!.favouriteDao().insert(favMovie)
                                    MovieDatabase.getInstance(view.context)!!.movieDao().update(movieEntity)
                                    val snackbar = Snackbar
                                        .make(view, "${favMovie.title} is added to favourite", Snackbar.LENGTH_LONG)
                                    snackbar.show()
                                } catch (e: Exception) {

                                    Toast.makeText(context, "Movie has been previously added \nto favorite", Toast.LENGTH_SHORT).show()
                                    val snackbar = Snackbar
                                        .make(view, "Movie has been previously added \n" +
                                                "to favorite", Snackbar.LENGTH_LONG)
                                    snackbar.show()
                                }


                            }


                        }
                        else{
                            fav.visibility = View.GONE
                            movieEntity.favourite = false
                            CoroutineScope(Main).launch {

                                try {
                                    MovieDatabase.getInstance(view.context)!!.favouriteDao().delete(favMovie)
                                    MovieDatabase.getInstance(view.context)!!.movieDao().update(movieEntity)
                                    val snackbar = Snackbar
                                        .make(view, "${favMovie.title} is removed from favourite", Snackbar.LENGTH_LONG)
                                    snackbar.show()
                                } catch (e: Exception) {

//                                        Toast.makeText(context, "Movie has been previously removed \nto favorite", Toast.LENGTH_SHORT).show()
                                    val snackbar = Snackbar
                                        .make(view, "Movie has been previously removed \n" +
                                                "to favorite", Snackbar.LENGTH_LONG)
                                    snackbar.show()
                                }


                            }
                        }

                    }

                    override fun onSingleClick(movieEntity: MovieEntity, view: View) {
                        val extras = FragmentNavigatorExtras(
                            view.findViewById<ImageView>(R.id.thumbnail) to movieEntity.movieId!!
                        )

                        val action = AllMoviesFragmentDirections.toMovieDetails()
                        action.movie = movieEntity
                        Navigation.findNavController(recylerView).navigate(action, extras)
                    }

                })
                recylerView.adapter = movieAdapter
                movieAdapter?.setMovie(list)

    //            Toast.makeText(context, "listi ${list?.last()?.title}", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {

            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val movie = movieAdapter?.getMovieAt(viewHolder.adapterPosition)
//                Toast.makeText(context, "swiped right", Toast.LENGTH_LONG).show()
                val extras = FragmentNavigatorExtras(
                    viewHolder.itemView.findViewById<ImageView>(R.id.thumbnail) to movie?.movieId!!
                )

                val action = AllMoviesFragmentDirections.toMovieDetails()
                action.movie = movie
                Navigation.findNavController(recylerView).navigate(action, extras)

            }

        }).attachToRecyclerView(recycler_view_movies)

        favouriteCard.setOnClickListener {
            val action = AllMoviesFragmentDirections.toFavouritePage()
            findNavController().navigate(action)
        }

    }

    override fun onStart() {
        super.onStart()


//        val recyclerMovies = view?.findViewById<RecyclerView>(R.id.recycler_view_movies)
//
//
//            recyclerMovies?.layoutManager = GridLayoutManager(context, 2)
//            recyclerMovies?.setHasFixedSize(true)
//            movieAdapter = MovieAdapter(list, object :MovieAdapter.OnMovieListener {
//                override fun onMovieClick(movieEntity: MovieEntity)
//
////        Toast.makeText(context, "onStart", Toast.LENGTH_SHORT).show()
//            }
    }

    override fun onResume() {
        super.onResume()

//        Toast.makeText(context, "onResume", Toast.LENGTH_SHORT).show()
    }
}


