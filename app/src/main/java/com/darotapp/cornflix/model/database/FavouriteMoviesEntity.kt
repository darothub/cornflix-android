package com.darotapp.cornflix.model.database

import android.icu.text.CaseMap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class FavouriteMoviesEntity(
    var title: String?,
    var movieImage:String?,
    var rating:Int?,
    var overView:String?,
    var releaseDate:String?


): Serializable {
    operator fun component1(): String? = title
    operator fun component2(): String? = movieImage
    operator fun component3(): Int? = rating
    operator fun component4(): String? = overView
    operator fun component5(): String? = releaseDate
    @PrimaryKey()
    var id: Int = 0
    var favourite:Boolean = false
    var movieId:String? = ""

}