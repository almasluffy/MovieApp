package com.example.kinopoisk.domain.models


data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val vote_average: Double,
    val original_language: String,
    val poster_path: String,
    val release_date: String,
    val vote_count: Int

) {
}