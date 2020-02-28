package com.darotapp.cornflix.ui


import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.graphics.toColorInt
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import kotlinx.android.synthetic.main.movie_recycler_items.view.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlin.random.Random

/**
 * A simple [Fragment] subclass.
 */
class AllMoviesFragment : Fragment() {
    //    var movieViewModel: MovieViewModel?=null
    var movieAdapter: MovieAdapter<MovieEntity>? = null
    var recyclerView: RecyclerView? = null
    var movies:List<MovieEntity>?=null
    var movieTitleList:List<String?>? = null
    var observable:MutableLiveData<List<MovieEntity>> = MutableLiveData<List<MovieEntity>>()
    lateinit var searchEditText: AutoCompleteTextView
    lateinit var searchBtn:Button
    lateinit var res: LiveData<List<MovieEntity>>
    var page = 0


    //View model factory to inject or instantiate movieViewModel
    private val movieViewModel by viewModels<MovieViewModel> {
        MovieViewModelfactory((requireContext().applicationContext as MovieApplication).moviesRepoInterface)
    }

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

        //Getting recyclerview
        recyclerView = requireView().findViewById<RecyclerView>(R.id.recycler_view_movies)


        searchEditText = requireView().findViewById(R.id.searchEditText)
        searchBtn = requireView().findViewById(R.id.searchBtn)



        observable = MutableLiveData()
        //Function to load data
        loadData(requireContext(), 1)

        //Observe live data and set into adapter
        observeAndSetData()

        // function for recyclerView ItemTouchHelper
        swipeItemTouchHelper()

        swipeItemTouchHelperForFavourite()
        //function to navigate to favourite fragment
        navigateToFavourite()

        onMenuClick()

        // function to search
        searchFun()

//        Log.i("onacti", "text $textOnScreen")

        loadMoreMovies()






    }

    private fun searchFun() {
        searchEditText.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                topRatedText.visibility = View.GONE
            }
            else{
                topRatedText.visibility = View.VISIBLE
            }
        }
        observable.observeForever {

            CoroutineScope(Main).launch {

                movies = it

                Log.i("movieSize", "${movies?.size}")
                // Extract title of movies
                movieTitleList = movies?.map {
                    it.title
                }


                //Autocompelete TextView adapter
                context?.let { it1 ->
                    setAutocompleteTextAdapter(movieTitleList as List<String>,
                        it1
                    )
                }
                searchBtnBehaviourOnclick(movies as List<MovieEntity>)


            }

            //Get movies available

        }

    }

    private suspend fun getLocalMovieList():List<MovieEntity> {
        val localMovies = ServiceLocator.createLocalDataSource(requireContext()).movieDao?.getMoviesList()
        Log.i("movieSize", "${localMovies?.size}")
        return localMovies as List<MovieEntity>
    }

    private fun searchBtnBehaviourOnclick(movies:List<MovieEntity>) {
        searchBtn.setOnClickListener {
            val text = searchEditText.text.toString()
            if(text.isNotEmpty()){

                val selectedMovie = movies.find {
                    text == it.title
                }

                if (selectedMovie != null) {
                    searchEditText.text.clear()
                    gotoDetails(selectedMovie)

                }
            }
            else{
                return@setOnClickListener
            }
        }
    }

    private fun setAutocompleteTextAdapter(moviesTitleList:List<String>, context: Context) {
        var autoCompleteAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1,
            moviesTitleList.toList()
        )
        searchEditText.setAdapter(autoCompleteAdapter)

    }


    private fun loadData(context: Context, page:Int){

        progress_bar.visible()
        recyclerView?.hide()

        val previousMoviesSize = movieViewModel.getMoviesSize(context)

        if(previousMoviesSize > 0){
            Log.i("fromLocal", "Loading $previousMoviesSize")
            return
        }
        else{
            Log.i("fromRemote", "Loading $previousMoviesSize")
            CoroutineScope(IO).launch {
                movieViewModel.loadMovies(context, page)
            }
        }



    }

    private fun loadMoreMovies(){

        swipeLayout.setOnRefreshListener {
            var pageNum =  Random.nextInt(1, 10)
            CoroutineScope(Main).launch {
                recyclerView?.visibility  = View.GONE
                withContext(IO){
                    movieViewModel.loadMovies(requireContext(), pageNum)
                }

                Log.i("page", "$pageNum")

                delay(3000)
                swipeLayout.isRefreshing = false

                recyclerView?.visibility  = View.VISIBLE



            }
        }

    }


    private fun observeAndSetData() {


        CoroutineScope(Main).launch {
            delay(1000)
            root_layout.setBackgroundColor(resources.getColor(R.color.colorAccent))
            progress_bar.hide()
            recyclerView?.visible()

            res = movieViewModel.getAllMovies(false, requireContext())
            res.observeForever {

                observable.value = it
                setDataIntoAdapter(it.filterNot {movie-> movie.favourite })
                recyclerView?.layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                recyclerView?.setHasFixedSize(true)
                recyclerView?.adapter = movieAdapter
                recyclerView?.itemAnimator = null

//                movieAdapter?.setMovie(it)
            }


        }


    }

    private fun setDataIntoAdapter(list: List<MovieEntity?>?) {



        movieAdapter = MovieAdapter(list, object : MovieAdapter.OnMovieListener {
            override fun onMovieDoubleClick(movieEntity: MovieEntity, view: View) {

                val fav = view.findViewById<ImageView>(R.id.redFav)
//                var favMovie = convertToFavourityEntity(movieEntity)
//                movieEntity.favourite = true
//                if (fav.visibility == View.GONE) {
//                    fav.visibility = View.VISIBLE
//
//
//                    CoroutineScope(Main).launch {
//
//                        try {
//                            insertAndUpdate(movieEntity)
//                            FancyToast.makeText(
//                                context,
//                                "${movieEntity.title} is added to favourite",
//                                FancyToast.LENGTH_LONG,
//                                FancyToast.SUCCESS,
//                                true
//                            ).show()
//                        } catch (e: Exception) {
//
//                            FancyToast.makeText(
//                                context,
//                                "Movie has been previously added \nto favorite",
//                                FancyToast.LENGTH_LONG,
//                                FancyToast.ERROR,
//                                true
//                            ).show()
//
//                        }
//
//
//                    }
//
//
//                } else {
//                    fav.visibility = View.GONE
//                    movieEntity.favourite = false
//                    CoroutineScope(Main).launch {
//
//                        try {
//
//                            deleteAndUpdate(movieEntity)
//                            FancyToast.makeText(
//                                context,
//                                "${movieEntity.title} is removed from favourite",
//                                FancyToast.LENGTH_LONG,
//                                FancyToast.INFO,
//                                true
//                            ).show()
//                        } catch (e: Exception) {
//
////                                        Toast.makeText(context, "Movie has been previously removed \nto favorite", Toast.LENGTH_SHORT).show()
//                            val snackbar = Snackbar
//                                .make(
//                                    view, "Movie has been previously removed \n" +
//                                            "to favorite", Snackbar.LENGTH_LONG
//                                )
//                            snackbar.show()
//                        }
//
//
//                    }
//                }

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

    private suspend fun insertAndUpdate(movieEntity: MovieEntity) {
        ServiceLocator.createLocalDataSource(requireContext()).movieDao?.update(movieEntity)

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
        favMovie.id = movieEntity.movieId?.toInt() ?: movieEntity.id
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
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.DOWN or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val movie = movieAdapter?.getMovieAt(viewHolder.adapterPosition)
                singleDeletion(movie)
                recyclerView?.scrollToPosition(viewHolder.adapterPosition)

            }

        }).attachToRecyclerView(recycler_view_movies)
    }

    //
    private suspend fun deleteAndUpdate(movieEntity: MovieEntity) {
        ServiceLocator.createLocalDataSource(requireContext()).movieDao?.update(movieEntity)


    }


    private fun onMenuClick(){
        allMoviesToolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.deleteAll ->{
                    if(movieTitleList.isNullOrEmpty()){
                        Toast.makeText(context, "No movie to delete", Toast.LENGTH_SHORT).show()
                    }
                    else{

                        AlertDialog.Builder(context).apply {
                            setTitle("Are you sure?")
                            setMessage("You can not undo this operation")
                            setPositiveButton("Yes"){_, _ ->

                                CoroutineScope(IO).launch {
                                    ServiceLocator.createLocalDataSource(requireContext()).movieDao?.deleteAllMovies(true)
                                }
                                Toast.makeText(context, "All movies deleted", Toast.LENGTH_SHORT).show()
                            }
                            setNegativeButton("No"){_, _ ->
                                findNavController().navigate(R.id.allMoviesFragment)

                            }


                        }.create().show()
                    }
                    true
                }
                else -> false
            }

        }
    }

    private fun singleDeletion(movieEntity: MovieEntity?){

        AlertDialog.Builder(context).apply {
            setTitle("Are you sure?")
            setMessage("This may also affect your favourite list")
            setPositiveButton("Yes"){_, _ ->

                CoroutineScope(IO).launch {
                    movieEntity?.let {
                        ServiceLocator.createLocalDataSource(context).movieDao?.delete(
                            it
                        )
                    }
                }
                Toast.makeText(context, "${movieEntity?.title} deleted", Toast.LENGTH_SHORT).show()
            }
            setNegativeButton("No"){_, _ ->
                findNavController().navigate(R.id.allMoviesFragment)

            }


        }.create().show()
    }


    private fun swipeItemTouchHelperForFavourite() {
        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.UP or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val movie = movieAdapter?.getMovieAt(viewHolder.adapterPosition)
                movie?.favourite = true
                CoroutineScope(IO).launch {
                    movie?.let { insertAndUpdate(it) }
                }

                FancyToast.makeText(
                    context,
                    "${movie?.title} is added to favourite",
                    FancyToast.LENGTH_LONG,
                    FancyToast.SUCCESS,
                    true
                ).show()
                recyclerView?.scrollToPosition(viewHolder.adapterPosition)

            }

        }).attachToRecyclerView(recycler_view_movies)
    }

    private fun View.visible(){
        this.visibility = View.VISIBLE
    }

    private fun View.hide(){
        this.visibility = View.GONE
    }

//    override fun onDestroy() {
//        super.onDestroy()
//
//        res.removeObservers(viewLifecycleOwner)
//    }
}




