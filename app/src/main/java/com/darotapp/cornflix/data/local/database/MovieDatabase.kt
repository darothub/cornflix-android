package com.darotapp.cornflix.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MovieEntity::class, FavouriteMoviesEntity::class], version = 4, exportSchema = false)
abstract class MovieDatabase:RoomDatabase() {

    abstract fun movieDao():MovieDao
    abstract fun favouriteDao():FavouriteDao

//    companion object{
//        @Volatile
//        private var instance:MovieDatabase? = null
//
//        fun getInstance(context: Context):MovieDatabase?{
//            if(instance == null){
//                instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    MovieDatabase::class.java, "movie_database"
//                )
//                    .fallbackToDestructiveMigration()
//                    .build()
//            }
//            return instance
//        }
//    }
}
