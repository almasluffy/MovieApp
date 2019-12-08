package com.example.kinopoisk.presentation.movie.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kinopoisk.base.BaseViewModel
import com.example.kinopoisk.data.api_services.ApiClient
import com.example.kinopoisk.data.models.MovieData
import com.example.kinopoisk.data.repository.MovieRepositoryImpl
import com.example.kinopoisk.domain.repository.MovieRepository
import com.example.kinopoisk.exceptions.NoConnectionException
import com.example.kinopoisk.extensions.launchSafe
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class FavoritesViewModel(
    private val movieRepository: MovieRepository
): BaseViewModel(){

    private val _liveData = MutableLiveData<State>()
    val liveData: LiveData<State>
        get() = _liveData

    override fun handleError(e: Throwable) {
        if (e is NoConnectionException) {
            _liveData.value = State.IntError(e.messageInt)
        } else {
            _liveData.value = State.Error(e.localizedMessage)
        }
    }


    fun loadFavMovies(accountId: Int?, sessionId: String?, page: Int = 1) {
        uiScope.launchSafe(::handleError) {
            val result = withContext(Dispatchers.IO) {
                val response =
                    accountId?.let { accountId ->
                        sessionId?.let{ sessionId ->
                            movieRepository.getFavoriteMovies(accountId, sessionId, page)
                        }
                    }
                val list = response?.results ?: emptyList()
                val totalPages = response?.totalPages ?: 0
                Pair(totalPages, list)
            }
            _liveData.postValue(
                State.Result(
                    totalPages = result.first,
                    list = result.second
                ))
        }
    }

    sealed class State {
        data class Result(val totalPages: Int, val list: List<MovieData>): State()
        data class Error(val error: String?): State()
        data class IntError(val error: Int): State()
    }
}