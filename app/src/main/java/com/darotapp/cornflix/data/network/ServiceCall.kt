package com.darotapp.cornflix.data.network

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface ServiceCall {

    suspend fun getMovies(context: Context) {

    }
}