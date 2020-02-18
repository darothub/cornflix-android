package com.darotapp.cornflix.adapters

import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.darotapp.cornflix.R
import com.darotapp.cornflix.model.database.FavouriteMoviesEntity
import com.darotapp.cornflix.model.database.MovieEntity
import com.pedromassango.doubleclick.DoubleClick
import com.pedromassango.doubleclick.DoubleClickListener
import com.squareup.picasso.Picasso
import java.util.*

class FavouriteMoviesAdapter(private var movies:List<FavouriteMoviesEntity?>?, private var listener:OnMovieListener):RecyclerView.Adapter<FavouriteMoviesAdapter.MovieHolder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.movie_recycler_items, parent, false)
        return FavouriteMoviesAdapter.MovieHolder(itemView)
    }

    override fun getItemCount(): Int {
        return movies!!.size
    }

    fun setMovie(movies: List<FavouriteMoviesEntity?>?){
        this.movies = movies as List<FavouriteMoviesEntity>
        notifyDataSetChanged()
    }
    fun getMovieAt(position: Int):FavouriteMoviesEntity?{
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

        fun bind(movieEntity:FavouriteMoviesEntity, listener: OnMovieListener){

//            var i = 0
            Log.i("favourite", "${movieEntity.favourite}")
            if(movieEntity.favourite){
                redFav.visibility = View.VISIBLE
            }



            val calendar = Calendar.getInstance()
            val dateReleased = movieEntity.releaseDate?.split("-")
            val year = dateReleased?.get(0)?.toInt()
            val month = dateReleased?.get(1)?.toInt()
            val day = dateReleased?.get(2)?.toInt()
            var newDate:Date?= null
            if (year != null) {
                if (month != null) {
                    if (day != null) {
                        calendar.set(year, month,day)
                    }
                }
                newDate = calendar.time
            }
            val displayDate = newDate.toString().substring(4..7) + year.toString()
            title.setText(movieEntity.title)
            releaseDate.setText(displayDate)
            ratingBar.numStars = 5
            val ratingNum = movieEntity.rating?.toFloat()?.div(2)
            rating.setText("${ratingNum!!}")
            ratingBar.rating = ratingNum
            Picasso.get().load(movieEntity.movieImage).into(imageThmbnail)

            itemView.setOnClickListener(DoubleClick(object : DoubleClickListener {
                override fun onDoubleClick(view: View?) {

                    Log.i("Dob", "Double clicked")

                    listener.onMovieClick(movieEntity, itemView)
                }

                override fun onSingleClick(view: View?) {
                    Log.i("click", "Single click")
                    listener.onSingleClick(movieEntity, itemView)
                }

            }))

//            itemView.setOnClickListener {
//
//                listener.onMovieClick(movieEntity, it)
//
//            }
        }

    }

    interface OnMovieListener{
        fun onMovieClick(movieEntity: FavouriteMoviesEntity, view:View)
        fun onSingleClick(movieEntity: FavouriteMoviesEntity, view: View)
    }


}


