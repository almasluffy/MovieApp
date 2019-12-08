package com.example.kinopoisk.data.repository


import com.example.kinopoisk.data.api_services.MovieApi
import com.example.kinopoisk.data.models.MovieData
import com.example.kinopoisk.data.models.MovieListResponse
import com.example.kinopoisk.domain.repository.MovieRepository
import com.google.gson.JsonObject


class MovieRepositoryImpl(
    private val movieApi: MovieApi
): MovieRepository {

    //Movie
    override suspend fun getPopularMovies(page: Int) =
        movieApi.getPopularMovies(page).await().body()

    override suspend fun getFavoriteMovies(
        accountId: Int,
        sessionId: String,
        page: Int
    ): MovieListResponse? =
        movieApi.getFavoriteMovies(accountId, sessionId, page).await().body()

    override suspend fun getMovieById(movieId: Int): MovieData? =
        movieApi.getMovie(movieId).await().body()

    override suspend fun rateMovie(movieId: Int, accountId: Int, sessionId: String, favorite: Boolean): Int? {
        val body = JsonObject().apply {
            addProperty("media_type", "movie")
            addProperty("media_id", movieId)
            addProperty("favorite", favorite)
        }
        val response = movieApi.rateMovie(accountId, sessionId, body).await()
        return response.body()?.getAsJsonPrimitive("status_code")?.asInt
    }
}