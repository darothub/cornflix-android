package com.darotapp.cornflix.utils

import android.content.Context
import com.darotapp.cornflix.model.database.MovieEntity
import com.google.gson.GsonBuilder

class SharedPrefManager(val context: Context?) {
    companion object {
        fun clearShared(context: Context?) {
            val sharedPrefs = context?.getSharedPreferences("secret", Context.MODE_PRIVATE)!!
            sharedPrefs.edit()
                .apply {
                    clear()
                    apply()
                }
        }

        fun <T> saveData(context: Context?, movieEntity: MovieEntity, key: T): String {
            val result: String
//           val  sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
            val sharedPrefs = context?.getSharedPreferences("secret", Context.MODE_PRIVATE)!!
            val gsonInstance = GsonBuilder().create()
            val json = gsonInstance.toJson(movieEntity)
            result = json


            sharedPrefs.edit()
                .apply {
                    putString(key.toString(), json)
                    putString("movie", json)
                    apply()
                }
            return result
        }

        fun <T> getData(context: T?, movieEntity: String): MovieEntity? {

//           val  sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
            val gsonInstance = GsonBuilder().create()

            return gsonInstance.fromJson(movieEntity, MovieEntity::class.java)

        }


    }

}