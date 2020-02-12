package com.darotapp.cornflix.model.database

import android.icu.text.CaseMap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class MovieEntity(
    var title: String?,
    var movieImage:String?,
    var rating:Int?,
    var overView:String?,
    var releaseDate:String?


): Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var favourite:Int? = 0
    var movieId:String? = ""

}