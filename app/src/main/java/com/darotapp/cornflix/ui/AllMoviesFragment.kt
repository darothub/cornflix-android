package com.darotapp.cornflix.ui


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.darotapp.cornflix.R
import com.darotapp.cornflix.adapters.MovieAdapter
import com.darotapp.cornflix.adapters.SwipeAdapter
import com.darotapp.cornflix.data.viewmodel.MovieViewModel
import com.darotapp.cornflix.model.database.MovieEntity
import kotlinx.android.synthetic.main.fragment_all_movies.*
import kotlinx.android.synthetic.main.fragment_landing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class AllMoviesFragment : Fragment() {
    var movieViewModel: MovieViewModel?=null
    var movieAdapter:MovieAdapter?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_movies, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        CoroutineScope(Dispatchers.Main).launch {
            movieViewModel!!.loadData(context!!)
//            MovieDatabase.getInstance(context!!)?.movieDao()!!.insert(MovieEntity("hello", "hi",4, "helllo"))
        }
    }

    override fun onStart() {
        super.onStart()
        val recyclerMovies = view?.findViewById<RecyclerView>(R.id.recycler_view_movies)

        movieViewModel!!.getAllMovies()!!.observeForever {list ->
//            Log.i("listi", "${list?.last()?.title}")

            recyclerMovies?.layoutManager = GridLayoutManager(context, 2)
            recyclerMovies?.setHasFixedSize(true)
            movieAdapter = MovieAdapter(list, object :MovieAdapter.OnMovieListener{
                override fun onMovieClick(movieEntity: MovieEntity) {

                    Toast.makeText(context, "${movieEntity.movieId} is clicked", Toast.LENGTH_LONG).show()

                }
            })
            recycler_view_movies.adapter = movieAdapter
            movieAdapter?.setMovie(list)
//            Toast.makeText(context, "listi ${list?.last()?.title}", Toast.LENGTH_LONG).show()
        }
    }
}


