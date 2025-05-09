package app.instamovies.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import app.instamovies.core.util.Resource
import app.instamovies.domain.usecase.GetListUseCase
import app.instamovies.domain.usecase.movie.GetPopularMoviesUseCase
import app.instamovies.domain.usecase.trending.GetTrendingPersonUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val getListUseCase: GetListUseCase,
        private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
        private val getTrendingPersonUseCase: GetTrendingPersonUseCase,
    ) : ViewModel() {
        var uiState by mutableStateOf(HomeUiState())
            private set

        init {
            getExplore()
            getPopularMovies()
            getTrendingPerson()
        }

        fun onEvent(event: HomeUiEvent) {
            when (event) {
                HomeUiEvent.OnRetry -> {
                    if (uiState.exploreResource is Resource.Error) {
                        getExplore()
                    }
                    if (uiState.popularMoviesResource is Resource.Error) {
                        getPopularMovies()
                    }
                    if (uiState.trendingPersonResource is Resource.Error) {
                        getTrendingPerson()
                    }
                }
            }
        }

        private fun getExplore() {
            getListUseCase(8257364).onEach { resource ->
                uiState = uiState.copy(exploreResource = resource)
            }.launchIn(viewModelScope)
        }

        private fun getPopularMovies() {
            val locale = Locale.getDefault()
            getPopularMoviesUseCase(region = locale.country).onEach { resource ->
                uiState = uiState.copy(popularMoviesResource = resource)
            }.launchIn(viewModelScope)
        }

        private fun getTrendingPerson() {
            getTrendingPersonUseCase().onEach { resource ->
                uiState = uiState.copy(trendingPersonResource = resource)
            }.launchIn(viewModelScope)
        }
    }
