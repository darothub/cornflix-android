package com.darotapp.cornflix.ui


import android.os.Bundle
import android.os.Handler
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



        Toast.makeText(context, "onActivity created", Toast.LENGTH_SHORT).show()
        var recylerView = view!!.findViewById<RecyclerView>(R.id.recycler_view_movies)
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
                    override fun onMovieClick(movieEntity: MovieEntity) {
                        val viewHolder:RecyclerView.ViewHolder?= null

                            i=i.plus(1)
                            val handler = Handler()
                            val runn = Runnable {
                                i = 0
                            }
                            if(i == 1){
                                Toast.makeText(context, "Single Clicked", Toast.LENGTH_SHORT).show()
                                handler.postDelayed(runn, 400)
                            }
                            else if(i == 2){
                                Toast.makeText(context, "Double Clicked", Toast.LENGTH_SHORT).show()




                            }
                            return


                    }
                })
                recylerView.adapter = movieAdapter
                movieAdapter?.setMovie(list)

    //            Toast.makeText(context, "listi ${list?.last()?.title}", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {

            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onStart() {
        super.onStart()

//        Toast.makeText(context, "onStart", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()

//        Toast.makeText(context, "onResume", Toast.LENGTH_SHORT).show()
    }
}


