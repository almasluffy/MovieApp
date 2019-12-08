package com.example.kinopoisk.presentation.movie.movie_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kinopoisk.base.BaseViewModel
import com.example.kinopoisk.data.api_services.ApiClient
import com.example.kinopoisk.data.models.MovieData
import com.example.kinopoisk.data.repository.MovieRepositoryImpl
import com.example.kinopoisk.domain.models.Movie
import com.example.kinopoisk.domain.repository.MovieRepository
import com.example.kinopoisk.exceptions.NoConnectionException
import com.example.kinopoisk.extensions.launchSafe
import com.example.kinopoisk.presentation.movie.favorites.FavoritesViewModel
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

class MovieDetailViewModel(
    private val movieRepository: MovieRepository
): BaseViewModel() {

    private val _liveData = MutableLiveData<State>()
    val liveData: LiveData<State>
        get() = _liveData

    fun getMovie(movieId: Int) {
        uiScope.launchSafe(::handleError) {
            _liveData.value =
                State.ShowLoading
            withContext(Dispatchers.IO) {
                val movie = movieRepository.getMovieById(movieId)
                movie?.let { movieData ->
                    _liveData.postValue (
                        State.Result(movieData)
                    )
                }
            }
            _liveData.value =
                State.HideLoading
        }
    }

    fun setFavorite(accountId: Int, movieId: Int, sessionId: String, favorite: Boolean) {
        uiScope.launchSafe(::handleError) {
            withContext(Dispatchers.Default) {
                val resultCode: Int? = movieRepository.rateMovie(movieId, accountId, sessionId, favorite)
                resultCode?.let { code ->
                    _liveData.postValue(
                        State.FavoriteMovie(
                            code
                        )
                    )
                }
            }
        }
    }

    override fun handleError(e: Throwable) {
        _liveData.value =
            State.HideLoading
        if (e is NoConnectionException) {
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

    sealed class State {
        object ShowLoading: State()
        object HideLoading: State()
        data class Result(val movie: MovieData): State()
        data class FavoriteMovie(val resultCode: Int): State()
        data class Error(val error: String?): State()
        data class IntError(val error: Int): State()
    }
}