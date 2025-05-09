package app.instamovies.presentation.movies

sealed class MoviesUiEvent {
    data object GetNowPlayingMovies : MoviesUiEvent()

    data object GetTopRatedMovies : MoviesUiEvent()

    data object GetUpcomingMovies : MoviesUiEvent()
}
