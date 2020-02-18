package com.darotapp.cornflix.data.viewmodel

import android.content.Context
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.darotapp.cornflix.model.database.MovieEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.withContext
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class MovieViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var movieViewModel: MovieViewModel

    @Before
    fun setUp() {
        movieViewModel = MovieViewModel(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun getAllMovies_setFireModelToTrue() {
        movieViewModel.getAllMovies()

        val value = movieViewModel.fireModel

        // Then the "Add task" action is visible
        assertThat(value.getOrAwaitValue(), Matchers.`is`(true))

    }

    @Test
    fun getAllMovies_setFireModelToNotNull() {

        movieViewModel.getAllMovies()

        val value = movieViewModel.fireModel
        value.observeForever {
            assertThat(it, CoreMatchers.not(Matchers.nullValue()))
        }

    }

    @Test
    fun getAllMovies_returnNonNullWhenObserved() {

        val listValue = MutableLiveData<List<MovieEntity?>>()
        CoroutineScope(Main).launch {
            withContext(IO) {
                movieViewModel.getAllMovies()?.observeForever {
                    listValue.value = it
                }
            }
            assertThat(listValue.getOrAwaitValue(), CoreMatchers.not(Matchers.nullValue()))
        }

    }

//    @Test
//    fun loadData() = runBlockingTest{
//        movieViewModel.loadData(ApplicationProvider.getApplicationContext())
//
//    }
//
//    @Test
//    fun getAllFav() {
//
//        movieViewModel.getAllFav()
//
//        val value = movieViewModel.fireModel
//
//        // Then the "Add task" action is visible
//        assertThat(value.getOrAwaitValue(), Matchers.`is`(true))
//        value.observeForever {
//            assertThat(it, CoreMatchers.not(Matchers.nullValue()))
//        }
//
//
//    }
}