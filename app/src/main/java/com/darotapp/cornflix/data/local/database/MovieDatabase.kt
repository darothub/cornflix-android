package com.darotapp.cornflix.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MovieEntity::class, FavouriteMoviesEntity::class], version = 4, exportSchema = false)
abstract class MovieDatabase:RoomDatabase() {

    abstract fun movieDao():MovieDao
}
