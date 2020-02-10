package com.darotapp.cornflix.model.database

import android.icu.text.CaseMap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class MovieEntity(
    var title: String?,
    var movieImage:String?,
    var rating:String?,
    var overView:String?


): Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var favourite:Int? = 0

}