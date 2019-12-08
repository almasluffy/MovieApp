package com.example.kinopoisk.domain.repository

import com.example.kinopoisk.data.models.AccountData
import com.example.kinopoisk.data.models.MovieData
import com.example.kinopoisk.data.models.MovieListResponse
import com.example.kinopoisk.domain.models.Movie
import com.google.gson.JsonObject
import retrofit2.Response

interface MovieRepository {

    suspend fun getPopularMovies(page: Int) : MovieListResponse?

    suspend fun getMovieById(movieId: Int): MovieData?

    suspend fun getFavoriteMovies(accountId: Int, sessionId: String, page: Int): MovieListResponse?

    suspend fun rateMovie(movieId: Int, accountId: Int, sessionId: String, favorite:Boolean): Int?
}