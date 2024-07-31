package instamovies.app.presentation.movieDetails

sealed class MovieDetailsUiEvent {
    data class OnCastAndCrewBottomSheetOpenChange(
        val open: Boolean,
    ) : MovieDetailsUiEvent()

    data object OnRetry : MovieDetailsUiEvent()
}
