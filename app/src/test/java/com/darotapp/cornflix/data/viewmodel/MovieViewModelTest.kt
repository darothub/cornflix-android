package com.darotapp.cornflix.data.viewmodel

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.darotapp.cornflix.data.local.database.MovieEntity
import com.darotapp.cornflix.data.repository.MovieRepository
import com.darotapp.cornflix.data.viewmodel.source.FakeDataSource
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
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class MovieViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movieRepositoryForTest: FakeRepositoryForTest


    private val movie1 = MovieEntity("Title1", "https://movie1.jpg", 3, "Movie1Overview", "Jan 2021")

    private val movie2 = MovieEntity("Title2", "https://movie2.jpg", 3, "Movie2Overview", "Jan 2022")
    private val movie3 = MovieEntity("Title3", "https://movie3.jpg", 3, "Movie2Overview", "Jan 2023")
    private val remoteTasks = mutableListOf(movie3).sortedBy { it.id }
    private val localTasks = mutableListOf(movie1, movie2).sortedBy { it.id }

    private lateinit var remoteDataSource: FakeDataSource<MovieEntity>
    private lateinit var localDataSource: FakeDataSource<MovieEntity>

    @Before
    fun setUp() {
        remoteDataSource = FakeDataSource(remoteTasks.toMutableList())
        localDataSource = FakeDataSource(localTasks.toMutableList())
        movieRepositoryForTest = FakeRepositoryForTest(remoteDataSource, localDataSource)
        movieViewModel = MovieViewModel(movieRepositoryForTest)

    }

    @Test
    fun getMoviesLocal_Returns_Movies_From_Local_DataSource()= runBlockingTest {
        val result=movieRepositoryForTest.getMovies(false, ApplicationProvider.getApplicationContext())

        assertThat(result.value, Matchers.equalTo(localTasks))

    }

    @Test
    fun getMoviesRemote_Returns_Movies_From_Remote_DataSource()= runBlockingTest {
        val result=movieRepositoryForTest.getMovies(true, ApplicationProvider.getApplicationContext())

        assertThat(result.value, Matchers.equalTo(remoteTasks))

    }


}