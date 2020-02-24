package com.darotapp.cornflix.ui


import android.graphics.Paint
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI

import com.darotapp.cornflix.R
import com.darotapp.cornflix.ServiceLocator
import com.darotapp.cornflix.data.local.database.FavouriteMoviesEntity
import com.darotapp.cornflix.data.local.database.MovieEntity
import com.pedromassango.doubleclick.DoubleClick
import com.pedromassango.doubleclick.DoubleClickListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.android.synthetic.main.fragment_movie_details.appBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class MovieDetailsFragment : Fragment() {
    var incomingMovie:MovieEntity?=null
//    var fromFav:FavouriteMoviesEntity?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_details, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val nav = Navigation.findNavController(appBar)
        NavigationUI.setupWithNavController(toolBar, nav)

        //Receives arguments(safeArgs) from other fragment
        arguments?.let{
            incomingMovie = MovieDetailsFragmentArgs.fromBundle(it).movie
        }


        //Receiving Navigator extras
        image.transitionName = incomingMovie?.movieId
        overView.transitionName = "overView"
        overView.alignment = Paint.Align.LEFT
        overView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0F)



        settingInIncomingMovie()
        addToFavFunction()



    }

    private fun settingInIncomingMovie() {
        val ratingValue =incomingMovie?.rating?.div(2)?.toFloat()
        incomingMovie?.let {
            if (it.favourite) redFav.visibility = View.VISIBLE
            overView.setText(it.overView)
            releaseDate.append(" ${it.releaseDate}")
            rating.append(" $ratingValue")
            ratingValue?.let {
                ratingBar.rating = it
            }
            Picasso.get().load(it.movieImage).into(image)
        }
    }

    private fun addToFavFunction() {
        image.setOnClickListener(DoubleClick(object :DoubleClickListener{
            override fun onDoubleClick(view: View?) {
                redFav.visibility = View.VISIBLE
                incomingMovie?.favourite = true
                CoroutineScope(IO).launch {
                    ServiceLocator.createLocalDataSource(context!!).movieDao?.update(incomingMovie as MovieEntity)
                }

            }

            override fun onSingleClick(view: View?) {
                redFav.visibility = View.GONE
                incomingMovie?.favourite = false
                CoroutineScope(IO).launch {
                    ServiceLocator.createLocalDataSource(context!!).movieDao?.update(incomingMovie as MovieEntity)
                }

            }

        }))
    }
//

}
