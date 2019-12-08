package com.example.kinopoisk.presentation.cinema.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.kinopoisk.base.BaseViewModel
import com.example.kinopoisk.data.CinemaDatabase.Cinema
import com.example.kinopoisk.data.CinemaDatabase.CinemaDao
import com.example.kinopoisk.data.CinemaDatabase.CinemaRoomDatabase
import com.example.kinopoisk.data.repository.CinemaRepositoryImpl
import com.example.kinopoisk.domain.repository.CinemaRepository
import com.example.kinopoisk.exceptions.NoConnectionException

class CinemaListViewModel(
    private val cinemaDao: CinemaDao
): BaseViewModel() {


    private val repository: CinemaRepository

    var liveData : LiveData<List<Cinema>>

    init {
        repository = CinemaRepositoryImpl(cinemaDao)
        liveData = repository.getAllCinemas()
    }

    override fun handleError(e: Throwable) {
        if (e is NoConnectionException) {
            //ToDo
        }
    }
}