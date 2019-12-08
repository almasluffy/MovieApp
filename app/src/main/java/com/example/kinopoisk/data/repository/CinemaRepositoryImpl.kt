package com.example.kinopoisk.data.repository

import androidx.lifecycle.LiveData
import com.example.kinopoisk.data.CinemaDatabase.Cinema
import com.example.kinopoisk.data.CinemaDatabase.CinemaDao
import com.example.kinopoisk.domain.repository.CinemaRepository

class CinemaRepositoryImpl(private val cinemaDao: CinemaDao) : CinemaRepository {

    override fun getAllCinemas(): LiveData<List<Cinema>> = cinemaDao.getCinemas()

    override fun getCinema(id: Int): LiveData<Cinema> = cinemaDao.getCinema(id)
}