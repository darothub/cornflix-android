package com.darotapp.cornflix.ui


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.darotapp.cornflix.R
import com.darotapp.cornflix.adapters.SwipeAdapter
import com.darotapp.cornflix.data.viewmodel.MovieViewModel
import com.darotapp.cornflix.model.database.MovieDatabase
import com.darotapp.cornflix.model.database.MovieEntity
import kotlinx.android.synthetic.main.fragment_landing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class LandingFragment : Fragment() {

    val adapter:SwipeAdapter? = null
    var movieViewModel:MovieViewModel?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_landing, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = SwipeAdapter()

        //Swipe listener for left swipe(to delete)
        ItemTouchHelper(object :ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
              Toast.makeText(context, "swiped left", Toast.LENGTH_LONG).show()


            }

        }).attachToRecyclerView(recycler_view)

        ItemTouchHelper(object :ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Toast.makeText(context, "swiped right", Toast.LENGTH_LONG).show()
                val action = LandingFragmentDirections.toAllMovies()
                Navigation.findNavController(recycler_view).navigate(action)

            }

        }).attachToRecyclerView(recycler_view)





    }

    override fun onStart() {
        super.onStart()

        Toast.makeText(context, "started", Toast.LENGTH_LONG).show()



    }
}
