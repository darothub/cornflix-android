package com.darotapp.cornflix.data.repository

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.darotapp.cornflix.data.viewmodel.getOrAwaitValue
import com.darotapp.cornflix.data.viewmodel.source.FakeDataSource
import com.darotapp.cornflix.data.local.database.MovieDao
import com.darotapp.cornflix.data.local.database.MovieEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class MovieRepositoryTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private val movie1 = MovieEntity("Title1", "https://movie1.jpg", 3, "Movie1Overview", "Jan 2021")
    private val movie2 = MovieEntity("Title2", "https://movie2.jpg", 3, "Movie2Overview", "Jan 2022")
    private val movie3 = MovieEntity("Title3", "https://movie3.jpg", 3, "Movie2Overview", "Jan 2023")
    private val remoteTasks = listOf(movie1, movie2, movie3).sortedBy { it.id }

    private lateinit var tasksRemoteDataSource: FakeDataSource
    private lateinit var moviesRepository: MovieRepository
    private lateinit var movieDao: MovieDao

    @Before
    fun createRepository() {
        tasksRemoteDataSource = FakeDataSource(remoteTasks.toMutableList())

    }

    @Test
    fun getRemoteMovies_RequestAllMovie()= runBlockingTest{
        val response = tasksRemoteDataSource.getDataFromRemote(ApplicationProvider.getApplicationContext())
        assertThat(response.getOrAwaitValue(), Matchers.`is`(true))
    }

}