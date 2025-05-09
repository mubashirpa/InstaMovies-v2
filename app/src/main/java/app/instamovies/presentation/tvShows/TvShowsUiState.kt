package app.instamovies.presentation.tvShows

import androidx.paging.PagingData
import app.instamovies.domain.model.series.SeriesResultModel
import kotlinx.coroutines.flow.MutableStateFlow

data class TvShowsUiState(
    val isAiringTodaySeriesLoaded: Boolean = false,
    val isOnTheAirSeriesLoaded: Boolean = false,
    val isTopRatedSeriesLoaded: Boolean = false,
    val airingTodaySeries: MutableStateFlow<PagingData<SeriesResultModel>> =
        MutableStateFlow(
            PagingData.empty(),
        ),
    val onTheAirSeries: MutableStateFlow<PagingData<SeriesResultModel>> =
        MutableStateFlow(
            PagingData.empty(),
        ),
    val popularSeries: MutableStateFlow<PagingData<SeriesResultModel>> =
        MutableStateFlow(
            PagingData.empty(),
        ),
    val topRatedSeries: MutableStateFlow<PagingData<SeriesResultModel>> =
        MutableStateFlow(
            PagingData.empty(),
        ),
)
