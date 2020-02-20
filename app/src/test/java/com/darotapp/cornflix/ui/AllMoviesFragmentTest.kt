package com.darotapp.cornflix.ui

import com.darotapp.cornflix.data.local.database.FavouriteMoviesEntity
import com.darotapp.cornflix.data.local.database.MovieEntity
import org.hamcrest.Matchers.instanceOf
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class AllMoviesFragmentTest {
    private lateinit var movie: MovieEntity
    private lateinit var allMoviesFrag:AllMoviesFragment
    @Before
    fun setUp(){
        movie = MovieEntity("Rising", "https:rising.jpg", 3, "Alot", "Jan 2020")
        allMoviesFrag = AllMoviesFragment()
    }

    @Test
    fun convertToFavourityEntity_Returns_FavouriteEntity() {
       val result = allMoviesFrag.convertToFavourityEntity(movie)
//        assertThat(result is FavouriteMoviesEntity, Matchers.`is`(true))
        assertThat(result, instanceOf(FavouriteMoviesEntity::class.java))
    }
}