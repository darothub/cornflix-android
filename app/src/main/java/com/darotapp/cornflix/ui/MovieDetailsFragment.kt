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
import com.darotapp.cornflix.data.local.database.FavouriteMoviesEntity
import com.darotapp.cornflix.data.local.database.MovieEntity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_movie_details.*
import kotlinx.android.synthetic.main.fragment_movie_details.appBar

/**
 * A simple [Fragment] subclass.
 */
class MovieDetailsFragment : Fragment() {
    var incomingMovie:MovieEntity?=null
    var fromFav:FavouriteMoviesEntity?=null

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
        //Receives arguments from other fragment
        arguments?.let{
            incomingMovie = MovieDetailsFragmentArgs.fromBundle(it).movie
            fromFav = MovieDetailsFragmentArgs.fromBundle(it).favMovie
        }
        val ratingValue =incomingMovie?.rating?.div(2)?.toFloat()
        val ratingValue2 =fromFav?.rating?.div(2)?.toFloat()

        image.transitionName = incomingMovie?.movieId
        overView.transitionName = "overView"
        overView.alignment = Paint.Align.LEFT
        overView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0F)




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
        fromFav?.let {
            if (it.favourite) redFav.visibility = View.VISIBLE
            overView.setText(it.overView)
            releaseDate.append(" ${it?.releaseDate}")
            rating.append(" $ratingValue2")
            ratingValue2?.let {
                ratingBar.rating = it
            }
            Picasso.get().load(it.movieImage).into(image)
        }








    }
//

}
