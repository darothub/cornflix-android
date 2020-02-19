package com.darotapp.cornflix.data.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.darotapp.cornflix.data.repository.MovieRepository
import com.darotapp.cornflix.data.repository.MoviesRepoInterface

class MovieViewModelfactory(
    private val moviesRepoInterface: MoviesRepoInterface
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (MovieViewModel(moviesRepoInterface) as T)
}
