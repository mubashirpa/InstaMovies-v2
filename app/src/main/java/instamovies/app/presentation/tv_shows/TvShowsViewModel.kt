package instamovies.app.presentation.tv_shows

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import instamovies.app.domain.usecase.series.GetAiringTodaySeriesUseCase
import instamovies.app.domain.usecase.series.GetOnTheAirSeriesUseCase
import instamovies.app.domain.usecase.series.GetPopularSeriesUseCase
import instamovies.app.domain.usecase.series.GetTopRatedSeriesUseCase
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class TvShowsViewModel
    @Inject
    constructor(
        private val getAiringTodaySeriesUseCase: GetAiringTodaySeriesUseCase,
        private val getOnTheAirSeriesUseCase: GetOnTheAirSeriesUseCase,
        private val getPopularSeriesUseCase: GetPopularSeriesUseCase,
        private val getTopRatedSeriesUseCase: GetTopRatedSeriesUseCase,
    ) : ViewModel() {
        var uiState by mutableStateOf(TvShowsUiState())
            private set

        private val timeZone = TimeZone.getDefault()

        init {
            getPopularSeries()
        }

        fun onEvent(event: TvShowsUiEvent) {
            when (event) {
                TvShowsUiEvent.GetAiringTodaySeries -> {
                    uiState = uiState.copy(isAiringTodaySeriesLoaded = true)
                    getAiringTodaySeries()
                }

                TvShowsUiEvent.GetOnTheAirSeries -> {
                    uiState = uiState.copy(isOnTheAirSeriesLoaded = true)
                    getOnTheAirSeries()
                }

                TvShowsUiEvent.GetTopRatedSeries -> {
                    uiState = uiState.copy(isTopRatedSeriesLoaded = true)
                    getTopRatedSeries()
                }
            }
        }

        private fun getAiringTodaySeries() {
            viewModelScope.launch {
                getAiringTodaySeriesUseCase(timeZone.id)
                    .distinctUntilChanged()
                    .cachedIn(viewModelScope)
                    .collect {
                        uiState.airingTodaySeries.value = it
                    }
            }
        }

        private fun getOnTheAirSeries() {
            viewModelScope.launch {
                getOnTheAirSeriesUseCase(timeZone.id)
                    .distinctUntilChanged()
                    .cachedIn(viewModelScope)
                    .collect {
                        uiState.onTheAirSeries.value = it
                    }
            }
        }

        private fun getPopularSeries() {
            viewModelScope.launch {
                getPopularSeriesUseCase()
                    .distinctUntilChanged()
                    .cachedIn(viewModelScope)
                    .collect {
                        uiState.popularSeries.value = it
                    }
            }
        }

        private fun getTopRatedSeries() {
            viewModelScope.launch {
                getTopRatedSeriesUseCase()
                    .distinctUntilChanged()
                    .cachedIn(viewModelScope)
                    .collect {
                        uiState.topRatedSeries.value = it
                    }
            }
        }
    }
