package com.darotapp.cornflix.data.repository

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.darotapp.cornflix.data.viewmodel.source.FakeDataSource
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
    private val remoteTasks = mutableListOf(movie3).sortedBy { it.id }
    private val localTasks = mutableListOf(movie1, movie2).sortedBy { it.id }

    private lateinit var remoteDataSource: FakeDataSource<MovieEntity>
    private lateinit var localDataSource: FakeDataSource<MovieEntity>
    private lateinit var moviesRepository: MoviesRepoInterface

    @Before
    fun createRepository() {

        remoteDataSource = FakeDataSource(remoteTasks.toMutableList())
        localDataSource = FakeDataSource(localTasks.toMutableList())
        moviesRepository = MovieRepository(remoteDataSource, localDataSource)


    }

    @Test
    fun getLocalMovies_RequestAllMoviesFromLocalDataSource()= runBlockingTest{


        val movies = localDataSource.getMovies(ApplicationProvider.getApplicationContext(), null)


        assertThat(movies?.value, Matchers.equalTo(localTasks))

    }

    @Test
    fun getLocalMovies_Returns_NotNull() = runBlockingTest {

        val movies = localDataSource.getMovies(ApplicationProvider.getApplicationContext(), null)

        assertThat(movies?.value, Matchers.notNullValue())
    }


}