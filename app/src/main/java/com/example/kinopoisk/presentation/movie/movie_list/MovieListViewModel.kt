package com.example.kinopoisk.presentation.movie.movie_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kinopoisk.base.BaseViewModel
import com.example.kinopoisk.data.models.MovieData
import com.example.kinopoisk.data.repository.MovieRepositoryImpl
import com.example.kinopoisk.domain.models.Movie
import com.example.kinopoisk.domain.repository.MovieRepository
import com.example.kinopoisk.exceptions.NoConnectionException
import com.example.kinopoisk.extensions.launchSafe
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MovieListViewModel(
    private val movieRepository: MovieRepository
): BaseViewModel() {

    private val _liveData = MutableLiveData<State>()
    val liveData: LiveData<State>
        get() = _liveData

    override fun handleError(e: Throwable) {
        _liveData.value =
            State.HideLoading
        if(e is NoConnectionException) {
            _liveData.value =
                State.IntError(
                    e.messageInt
                )
        } else {
            _liveData.value =
                State.Error(
                    e.localizedMessage
                )
        }
    }

    init {
        loadMovies()
    }

    fun loadMovies(page: Int = 1) {
        uiScope.launchSafe(::handleError) {
            if (page == 1) {
                _liveData.value =
                    State.ShowLoading
            }
            val result = withContext(Dispatchers.IO) {
                val response = movieRepository.getPopularMovies(page)
                val list = response?.results ?: emptyList()
                val totalPages = response?.totalPages ?: 0
                Pair(totalPages, list)
            }
            _liveData.postValue(
                State.Result(
                    totalPage = result.first,
                    list = result.second
                )
            )
            _liveData.value =
                State.HideLoading
        }
    }

    sealed class State {
        object ShowLoading: State()
        object HideLoading: State()
        data class Result(val totalPage: Int, val list: List<MovieData>): State()
        data class Error(val error: String?): State()
        data class IntError(val error: Int): State()
    }
}