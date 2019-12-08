package com.example.kinopoisk.data.models

import com.example.kinopoisk.data.models.MovieData
import com.example.kinopoisk.domain.models.Movie
import com.google.gson.annotations.SerializedName

data class MovieListResponse(
    @SerializedName("page") val page: Int?,
    @SerializedName("total_pages") val totalPages: Int?,
    @SerializedName("total_results") val totalResults: Int?,
    @SerializedName("results") val results: List<MovieData>?
)
