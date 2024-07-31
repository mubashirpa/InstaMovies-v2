package instamovies.app.presentation.tvShows

sealed class TvShowsUiEvent {
    data object GetAiringTodaySeries : TvShowsUiEvent()

    data object GetOnTheAirSeries : TvShowsUiEvent()

    data object GetTopRatedSeries : TvShowsUiEvent()
}
