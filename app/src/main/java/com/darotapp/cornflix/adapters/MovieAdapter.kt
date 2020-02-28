package com.darotapp.cornflix.adapters

import android.graphics.Movie
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.darotapp.cornflix.R
import com.darotapp.cornflix.adapters.utils.MovieDiffUtilCallBack
import com.darotapp.cornflix.data.local.database.MovieEntity
import com.pedromassango.doubleclick.DoubleClick
import com.pedromassango.doubleclick.DoubleClickListener
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.movie_recycler_items.*
import java.util.*

class MovieAdapter<T>(private var movies:List<T?>?, private var listener:OnMovieListener):RecyclerView.Adapter<MovieAdapter.MovieHolder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.movie_recycler_items, parent, false)
        return MovieAdapter.MovieHolder(itemView)
    }

    override fun getItemCount(): Int {
        return movies!!.size
    }

    fun setMovie(movies: List<T?>?){
        this.movies = movies
        notifyDataSetChanged()
    }
    fun getMovieAt(position: Int):T?{
        return movies?.get(position)
    }
    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
//        covert.drawCornerFlag(holder)
        movies?.let{
            val currentMovies = it[position]
            holder.bind(it[position]!!, listener)
        }
    }

    class MovieHolder(override val containerView: View?): RecyclerView.ViewHolder(containerView!!), LayoutContainer {


//        var title = itemView.findViewById<TextView>(R.id.title)
//        var releaseDate = itemView.findViewById<TextView>(R.id.releaseDate)
//        var ratingBar = itemView.findViewById<RatingBar>(R.id.ratingBar)
//        var imageThmbnail = itemView.findViewById<ImageView>(R.id.thumbnail)
//        var fav = itemView.findViewById<ImageView>(R.id.favourite)
//        var redFav = itemView.findViewById<ImageView>(R.id.redFav)
//        var rating = itemView.findViewById<TextView>(R.id.rating)

        fun <T> bind(movieEntity: T, listener: OnMovieListener){

            var i = 0


            if(movieEntity is MovieEntity){
                if(movieEntity.favourite){
                    redFav.visibility = View.VISIBLE
                }
                else{
                    redFav.visibility = View.GONE
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
                Picasso.get().load(movieEntity.movieImage).into(thumbnail)
                itemView.setOnClickListener(DoubleClick(object :DoubleClickListener{
                    override fun onDoubleClick(view: View?) {

                        Log.i("Dob", "Double clicked")

                        listener.onMovieDoubleClick(movieEntity, itemView)

                    }

                    override fun onSingleClick(view: View?) {
                        Log.i("click", "Single click")
                        listener.onSingleClick(movieEntity, itemView)

                    }

                }))

            }


        }

    }

    interface OnMovieListener{
        fun onMovieDoubleClick(movieEntity: MovieEntity, view:View)
        fun onSingleClick(movieEntity: MovieEntity, view: View)
    }


    fun insertItem(newList: List<MovieEntity>){
        val diffUtilCallBack = MovieDiffUtilCallBack(movies as List<MovieEntity>, newList)

        val diffUtilResult = DiffUtil.calculateDiff(diffUtilCallBack)

        val newMovies = movies?.toMutableList() as MutableList<MovieEntity>
        newMovies.addAll(newList)
        movies = newMovies.toList() as List<T?>

        diffUtilResult.dispatchUpdatesTo(this)

    }

    fun updateItem(newList: List<MovieEntity>){
        val diffUtilCallBack = MovieDiffUtilCallBack(movies as List<MovieEntity>, newList)

        val diffUtilResult = DiffUtil.calculateDiff(diffUtilCallBack)

        val newMovies = movies?.toMutableList() as MutableList<MovieEntity>

        newMovies.clear()
        newMovies.addAll(newList)
        movies = newMovies.toList() as List<T?>

        diffUtilResult.dispatchUpdatesTo(this)

    }


}


