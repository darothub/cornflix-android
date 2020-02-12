package com.darotapp.cornflix.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.darotapp.cornflix.R
import com.darotapp.cornflix.model.database.MovieEntity
import com.squareup.picasso.Picasso

class MovieAdapter(private var movies:List<MovieEntity?>?, private var listener:OnMovieListener):RecyclerView.Adapter<MovieAdapter.MovieHolder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.movie_recycler_items, parent, false)
        return MovieAdapter.MovieHolder(itemView)
    }

    override fun getItemCount(): Int {
        return movies!!.size
    }

    fun setMovie(movies: List<MovieEntity?>?){
        this.movies = movies as List<MovieEntity>
        notifyDataSetChanged()
    }
    fun getMovieAt(position: Int):MovieEntity?{
        return movies?.get(position)
    }
    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        movies?.let{
            val currentMovies = it[position]
            holder.bind(it[position]!!, listener)
        }
    }

    class MovieHolder(itemView: View): RecyclerView.ViewHolder(itemView) {


        var title = itemView.findViewById<TextView>(R.id.title)
        var releaseDate = itemView.findViewById<TextView>(R.id.releaseDate)
        var ratingBar = itemView.findViewById<RatingBar>(R.id.ratingBar)
        var imageThmbnail = itemView.findViewById<ImageView>(R.id.thumbnail)
        var fav = itemView.findViewById<ImageView>(R.id.favourite)
        var redFav = itemView.findViewById<ImageView>(R.id.redFav)
        var rating = itemView.findViewById<TextView>(R.id.rating)

        fun bind(movieEntity: MovieEntity, listener: OnMovieListener){


            fav.setOnClickListener{
                if(fav.visibility == View.VISIBLE){
                    fav.visibility = View.GONE
                    redFav.visibility = View.VISIBLE
                }
                else{
                    fav.visibility == View.VISIBLE
                    redFav.visibility = View.GONE
                }
            }

            redFav.setOnClickListener{
                if(redFav.visibility == View.VISIBLE){
                    redFav.visibility = View.GONE
                    fav.visibility = View.VISIBLE
                }
                else{
                    redFav.visibility == View.VISIBLE
                    fav.visibility = View.GONE
                }
            }
            title.setText(movieEntity.title)
            releaseDate.setText(movieEntity.releaseDate)
            ratingBar.numStars = 5
            val ratingNum = movieEntity.rating?.toFloat()?.div(2)
            rating.setText("${ratingNum!!}")
            ratingBar.rating = ratingNum
            Picasso.get().load(movieEntity.movieImage).into(imageThmbnail)

            itemView.setOnClickListener {
                listener.onMovieClick(movieEntity)
            }
        }

    }

    interface OnMovieListener{
        fun onMovieClick(movieEntity: MovieEntity)
    }


}


