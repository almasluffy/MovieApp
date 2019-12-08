package com.example.kinopoisk.domain.repository

import androidx.lifecycle.LiveData
import com.example.kinopoisk.data.CinemaDatabase.Cinema

interface CinemaRepository {

    fun getAllCinemas(): LiveData<List<Cinema>>

    fun getCinema(id: Int): LiveData<Cinema>

}