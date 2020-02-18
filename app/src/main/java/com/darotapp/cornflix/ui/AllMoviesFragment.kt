package com.darotapp.cornflix.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.darotapp.cornflix.R
import com.darotapp.cornflix.adapters.MovieAdapter
import com.darotapp.cornflix.data.viewmodel.MovieViewModel
import com.darotapp.cornflix.model.database.FavouriteMoviesEntity
import com.darotapp.cornflix.model.database.MovieDatabase
import com.darotapp.cornflix.model.database.MovieEntity
import com.google.android.material.snackbar.Snackbar
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.fragment_all_movies.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class AllMoviesFragment : Fragment() {
    var movieViewModel: MovieViewModel?=null
    var movieAdapter:MovieAdapter<MovieEntity>?=null
    var recyclerView:RecyclerView?=null
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
        recyclerView = view!!.findViewById<RecyclerView>(R.id.recycler_view_movies)



        loadData()
        observeAndSetData()
        swipeItemTouchHelper()
        navigateToFavourite()

    }

    private fun loadData(){
        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        CoroutineScope(Dispatchers.Main).launch {
            movieViewModel!!.loadData(context!!)
//            MovieDatabase.getInstance(context!!)?.movieDao()!!.insert(MovieEntity("hello", "hi",4, "helllo"))
        }
    }

    private fun observeAndSetData(){
//        val recylerView = view!!.findViewById<RecyclerView>(R.id.recycler_view_movies)
        try {

            movieViewModel!!.getAllMovies()!!.observeForever { list ->
                //            Log.i("listi", "${list?.last()?.title}")
                setDataIntoAdapter(list)
                recyclerView?.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                recyclerView?.setHasFixedSize(true)
                recyclerView?.adapter = movieAdapter
                movieAdapter?.setMovie(list)

                //            Toast.makeText(context, "listi ${list?.last()?.title}", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {

            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private  fun setDataIntoAdapter(list:List<MovieEntity?>?){


        movieAdapter = MovieAdapter(list, object :MovieAdapter.OnMovieListener{
            override fun onMovieDoubleClick(movieEntity: MovieEntity, view: View) {

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
                            FancyToast.makeText(context,"${favMovie.title} is added to favourite",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show()
                        } catch (e: Exception) {

                            FancyToast.makeText(context,"Movie has been previously added \nto favorite", FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show()

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
                            FancyToast.makeText(context,"${favMovie.title} is removed from favourite", FancyToast.LENGTH_LONG,FancyToast.INFO,true).show()
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
                recyclerView?.let {
                    Navigation.findNavController(it).navigate(action, extras)
                }

            }

        })
    }

    private fun navigateToFavourite() {
        favouriteCard.setOnClickListener {
            val action = AllMoviesFragmentDirections.toFavouritePage()
            findNavController().navigate(action)
        }
    }


    private fun swipeItemTouchHelper(){
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
                recyclerView?.let {
                    Navigation.findNavController(it).navigate(action, extras)
                }

            }

        }).attachToRecyclerView(recycler_view_movies)
    }
}


