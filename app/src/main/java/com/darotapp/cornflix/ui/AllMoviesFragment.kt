package com.darotapp.cornflix.ui


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.darotapp.cornflix.MovieApplication
import com.darotapp.cornflix.R
import com.darotapp.cornflix.ServiceLocator
import com.darotapp.cornflix.adapters.MovieAdapter
import com.darotapp.cornflix.data.viewmodel.MovieViewModel
import com.darotapp.cornflix.data.local.database.FavouriteMoviesEntity
import com.darotapp.cornflix.data.local.database.MovieDatabase
import com.darotapp.cornflix.data.local.database.MovieEntity
import com.darotapp.cornflix.data.viewmodel.MovieViewModelfactory
import com.google.android.material.snackbar.Snackbar
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.fragment_all_movies.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class AllMoviesFragment : Fragment() {
    //    var movieViewModel: MovieViewModel?=null
    var movieAdapter: MovieAdapter<MovieEntity>? = null
    var recyclerView: RecyclerView? = null
    private val movieViewModel by viewModels<MovieViewModel> {
        MovieViewModelfactory((requireContext().applicationContext as MovieApplication).moviesRepoInterface)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        observeAndSetData()

        return inflater.inflate(R.layout.fragment_all_movies, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        //Attaching navigation to the app bar and toolbar
        val nav = Navigation.findNavController(post_appbar)
        NavigationUI.setupWithNavController(allMoviesToolbar, nav)

//        Toast.makeText(context, "onActivity created", Toast.LENGTH_SHORT).show()
        recyclerView = view!!.findViewById<RecyclerView>(R.id.recycler_view_movies)

//        val movieEntity = MovieEntity("Rising", "rising.jpg", 3, "Wloooo", "jan 2020")



        loadData(context!!, 2)

        swipeItemTouchHelper()
        navigateToFavourite()


    }

    private fun loadData(context: Context, page:Int){
        CoroutineScope(IO).launch {
            movieViewModel.loadMovies(context, page)
        }
    }

    private fun observeAndSetData() {

        CoroutineScope(Main).launch {
            val res = movieViewModel.getAllMovies(context!!)
            res.observeForever {
//                Log.i("allFragment", "${it.get(0).title}")
                setDataIntoAdapter(it)
                recyclerView?.layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                recyclerView?.setHasFixedSize(true)
                recyclerView?.adapter = movieAdapter

//                movieAdapter?.setMovie(it)
            }

        }


    }

    private fun setDataIntoAdapter(list: List<MovieEntity?>?) {


        movieAdapter = MovieAdapter(list, object : MovieAdapter.OnMovieListener {
            override fun onMovieDoubleClick(movieEntity: MovieEntity, view: View) {

                val fav = view.findViewById<ImageView>(R.id.redFav)
                var favMovie = convertToFavourityEntity(movieEntity)


                if (fav.visibility == View.GONE) {
                    fav.visibility = View.VISIBLE


                    CoroutineScope(Main).launch {

                        try {
                            insertAndUpdate(favMovie, movieEntity)
                            FancyToast.makeText(
                                context,
                                "${favMovie.title} is added to favourite",
                                FancyToast.LENGTH_LONG,
                                FancyToast.SUCCESS,
                                true
                            ).show()
                        } catch (e: Exception) {

                            FancyToast.makeText(
                                context,
                                "Movie has been previously added \nto favorite",
                                FancyToast.LENGTH_LONG,
                                FancyToast.ERROR,
                                true
                            ).show()

                        }


                    }


                } else {
                    fav.visibility = View.GONE
                    movieEntity.favourite = false
                    CoroutineScope(Main).launch {

                        try {

                            deleteAndUpdate(favMovie, movieEntity)
                            FancyToast.makeText(
                                context,
                                "${favMovie.title} is removed from favourite",
                                FancyToast.LENGTH_LONG,
                                FancyToast.INFO,
                                true
                            ).show()
                        } catch (e: Exception) {

//                                        Toast.makeText(context, "Movie has been previously removed \nto favorite", Toast.LENGTH_SHORT).show()
                            val snackbar = Snackbar
                                .make(
                                    view, "Movie has been previously removed \n" +
                                            "to favorite", Snackbar.LENGTH_LONG
                                )
                            snackbar.show()
                        }


                    }
                }

            }

            override fun onSingleClick(movieEntity: MovieEntity, view: View) {


                gotoDetails(movieEntity)

            }

        })
    }

    //
    private fun gotoDetails(movieEntity: MovieEntity) {
        val extras = FragmentNavigatorExtras(
            view!!.findViewById<ImageView>(R.id.thumbnail) to movieEntity.movieId!!
        )
        val action = AllMoviesFragmentDirections.toMovieDetails()
        action.movie = movieEntity
        recyclerView?.let {
            Navigation.findNavController(it).navigate(action, extras)
        }
    }

    private suspend fun insertAndUpdate(favMovie: FavouriteMoviesEntity, movieEntity: MovieEntity) {
//        MovieDatabase.getInstance(context!!)?.movieDao()?.update(movieEntity)
//        MovieDatabase.getInstance(context!!)?.favouriteDao()?.insert(favMovie)
        ServiceLocator.createLocalDataSource(context!!).favouriteDao?.insert(favMovie)
        ServiceLocator.createLocalDataSource(context!!).movieDao?.update(movieEntity)

    }

    //
    fun convertToFavourityEntity(movieEntity: MovieEntity): FavouriteMoviesEntity {
        movieEntity.favourite = true

        val (title, movieImage, rating, overView, releaseDate) = movieEntity
        val favMovie = FavouriteMoviesEntity(
            title,
            movieImage,
            rating,
            overView,
            releaseDate
        )
        favMovie.movieId = movieEntity.movieId
        favMovie.id = movieEntity.movieId?.toInt() ?: movieEntity.id!!
        favMovie.favourite = movieEntity.favourite
        return favMovie
    }

    //
    private fun navigateToFavourite() {
        favouriteCard.setOnClickListener {
            val action = AllMoviesFragmentDirections.toFavouritePage()
            findNavController().navigate(action)
        }
    }

    //
//
    private fun swipeItemTouchHelper() {
        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val movie = movieAdapter?.getMovieAt(viewHolder.adapterPosition)
                gotoDetails(movie!!)

            }

        }).attachToRecyclerView(recycler_view_movies)
    }

    //
    private suspend fun deleteAndUpdate(favMovie: FavouriteMoviesEntity, movieEntity: MovieEntity) {
//
        ServiceLocator.createLocalDataSource(context!!).favouriteDao?.delete(favMovie)
        ServiceLocator.createLocalDataSource(context!!).movieDao?.update(movieEntity)
//        MovieDatabase.getInstance(context!!)?.movieDao()?.update(movieEntity)
//        MovieDatabase.getInstance(context!!)?.favouriteDao()?.delete(favMovie)

    }
}




