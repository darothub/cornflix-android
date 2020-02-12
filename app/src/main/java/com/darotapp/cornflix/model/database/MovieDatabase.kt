package com.darotapp.cornflix.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MovieEntity::class], version = 20, exportSchema = false)
abstract class MovieDatabase:RoomDatabase() {

    abstract fun movieDao():MovieDao

    companion object{
        private var instance:MovieDatabase? = null

        fun getInstance(context: Context):MovieDatabase?{
            if(instance == null){
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java, "movie_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }
}
