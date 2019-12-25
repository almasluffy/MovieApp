package com.example.kinopoisk.presentation.cinema.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.kinopoisk.data.CinemaDatabase.Cinema
import com.example.kinopoisk.data.CinemaDatabase.CinemaDao
import com.example.kinopoisk.data.CinemaDatabase.CinemaRoomDatabase
import com.example.kinopoisk.data.repository.CinemaRepositoryImpl
import com.example.kinopoisk.domain.repository.CinemaRepository
import kotlinx.coroutines.launch

class CinemaDetailViewModel(application: Application,private val cinemaDao: CinemaDao
) : AndroidViewModel(application) {

    private val repository: CinemaRepository

    lateinit var liveData: LiveData<Cinema>

    init {
        val cinemaDao = CinemaRoomDatabase.getDatabase(application, viewModelScope).cinemaDao()
        repository = CinemaRepositoryImpl(cinemaDao)
    }

    fun getCinema(id: Int) {
        viewModelScope.launch {
            val cinema = repository.getCinema(id)
            cinema.let { cinema ->
                liveData = cinema
            }
        }
    }
}