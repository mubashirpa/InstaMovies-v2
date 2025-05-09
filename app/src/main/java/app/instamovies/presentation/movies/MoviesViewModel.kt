package app.instamovies.presentation.movies

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import app.instamovies.domain.usecase.movie.GetNowPlayingMoviesUseCase
import app.instamovies.domain.usecase.movie.GetPopularMoviesPagingUseCase
import app.instamovies.domain.usecase.movie.GetTopRatedMoviesUseCase
import app.instamovies.domain.usecase.movie.GetUpcomingMoviesUseCase
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel
    @Inject
    constructor(
        private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
        private val getPopularMoviesPagingUseCase: GetPopularMoviesPagingUseCase,
        private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
        private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
    ) : ViewModel() {
        var uiState by mutableStateOf(MoviesUiState())
            private set

        private val locale = Locale.getDefault()

        init {
            getPopularMovies()
        }

        fun onEvent(event: MoviesUiEvent) {
            when (event) {
                MoviesUiEvent.GetNowPlayingMovies -> {
                    uiState = uiState.copy(isNowPlayingMoviesLoaded = true)
                    getNowPlayingMovies()
                }

                MoviesUiEvent.GetTopRatedMovies -> {
                    uiState = uiState.copy(isTopRatedMoviesLoaded = true)
                    getTopRatedMovies()
                }

                MoviesUiEvent.GetUpcomingMovies -> {
                    uiState = uiState.copy(isUpcomingMoviesLoaded = true)
                    getUpcomingMovies()
                }
            }
        }

        private fun getNowPlayingMovies() {
            viewModelScope.launch {
                getNowPlayingMoviesUseCase(region = locale.country)
                    .distinctUntilChanged()
                    .cachedIn(viewModelScope)
                    .collect {
                        uiState.nowPlayingMovies.value = it
                    }
            }
        }

        private fun getPopularMovies() {
            viewModelScope.launch {
                getPopularMoviesPagingUseCase(region = locale.country)
                    .distinctUntilChanged()
                    .cachedIn(viewModelScope)
                    .collect {
                        uiState.popularMovies.value = it
                    }
            }
        }

        private fun getTopRatedMovies() {
            viewModelScope.launch {
                getTopRatedMoviesUseCase(region = locale.country)
                    .distinctUntilChanged()
                    .cachedIn(viewModelScope)
                    .collect {
                        uiState.topRatedMovies.value = it
                    }
            }
        }

        private fun getUpcomingMovies() {
            viewModelScope.launch {
                getUpcomingMoviesUseCase(region = locale.country)
                    .distinctUntilChanged()
                    .cachedIn(viewModelScope)
                    .collect {
                        uiState.upcomingMovies.value = it
                    }
            }
        }
    }
