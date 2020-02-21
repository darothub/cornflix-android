package com.darotapp.cornflix.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.darotapp.cornflix.MovieApplication
import com.darotapp.cornflix.R
import com.darotapp.cornflix.ServiceLocator
import com.darotapp.cornflix.adapters.FavouriteMoviesAdapter
import com.darotapp.cornflix.adapters.MovieAdapter
import com.darotapp.cornflix.data.viewmodel.MovieViewModel
import com.darotapp.cornflix.data.local.database.FavouriteMoviesEntity
import com.darotapp.cornflix.data.local.database.MovieDatabase
import com.darotapp.cornflix.data.local.database.MovieEntity
import com.darotapp.cornflix.data.viewmodel.MovieViewModelfactory
import com.google.android.material.snackbar.Snackbar
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.fragment_favourite_movies.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 */
class FavouriteMoviesFragment : Fragment() {

    var favMovieAdapter: MovieAdapter<MovieEntity>? = null
    private var recyclerView: RecyclerView? = null
    private val movieViewModel by viewModels<MovieViewModel> {
        MovieViewModelfactory((requireContext().applicationContext as MovieApplication).moviesRepoInterface)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        observeAndSetData()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite_movies, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val nav = Navigation.findNavController(appBar)
        NavigationUI.setupWithNavController(favToolbar, nav)

        recyclerView = view!!.findViewById<RecyclerView>(R.id.recycler_view_fav)


        requireActivity().onBackPressedDispatcher.addCallback(this, object :OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.landingFragment)
            }

        })


    }


    private fun observeAndSetData() {

        try {
            CoroutineScope(Main).launch {
                movieViewModel.getAllFavMovies(context!!)?.observeForever { list ->

                    if (list.isNullOrEmpty()) {
//                    Toast.makeText(context, "You have not added any movie", Toast.LENGTH_SHORT).show()
                        try {
                            placeHolderFav.visibility = View.VISIBLE
                            recyclerView?.visibility = View.GONE
                            val snackbar = Snackbar
                                .make(appBar, "Favourite list is empty", Snackbar.LENGTH_LONG)
                            snackbar.show()
                        } catch (e: Exception) {
                        }
                    } else {
                        setDataIntoAdapter(list)
                        setRecyclerViewLayoutManager(list)

                    }


                }
            }

        } catch (e: Exception) {
        }

    }

    private fun setRecyclerViewLayoutManager(list: List<MovieEntity?>?) {
        recyclerView?.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = favMovieAdapter


//        favMovieAdapter?.setMovie(list)
    }

    private fun setDataIntoAdapter(list: List<MovieEntity?>?) {
        favMovieAdapter =
            MovieAdapter(list, object : MovieAdapter.OnMovieListener {
                override fun onMovieDoubleClick(movieEntity: MovieEntity, view: View) {

                    val fav = view.findViewById<ImageView>(R.id.redFav)
                    fav.visibility = View.GONE
                    movieEntity.favourite = false

                    CoroutineScope(Dispatchers.Main).launch {
                        actAndUpdateChanges(movieEntity)
                    }
//                    findNavController().navigate(R.id.favouriteMoviesFragment)


                    FancyToast.makeText(
                        context,
                        "${movieEntity.title} is removed from favourite",
                        FancyToast.LENGTH_LONG,
                        FancyToast.INFO,
                        true
                    ).show()
//                            Toast.makeText(context, "${movieEntity.title} has been removed", Toast.LENGTH_SHORT).show()

                }

                override fun onSingleClick(movieEntity: MovieEntity, view: View) {
                    navigateToDetails(movieEntity, view)
                }


            })
    }

    private fun navigateToDetails(movieEntity: MovieEntity, view: View) {
        val action =
            FavouriteMoviesFragmentDirections.actionFavouriteMoviesFragmentToMovieDetailsFragment()
        action.movie = movieEntity
        recyclerView?.let {
            Navigation.findNavController(it).navigate(action)
        }
    }

    suspend fun actAndUpdateChanges(updatedMovie: MovieEntity) {
        ServiceLocator.createLocalDataSource(context!!).movieDao?.update(updatedMovie)
        favMovieAdapter?.notifyDataSetChanged()
    }



}
