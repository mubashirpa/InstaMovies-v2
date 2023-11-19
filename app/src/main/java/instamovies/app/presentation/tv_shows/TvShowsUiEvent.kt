package instamovies.app.presentation.tv_shows

sealed class TvShowsUiEvent {
    data object GetAiringTodaySeries : TvShowsUiEvent()

    data object GetOnTheAirSeries : TvShowsUiEvent()

    data object GetTopRatedSeries : TvShowsUiEvent()
}
