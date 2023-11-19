package instamovies.app.presentation.movie_details

sealed class MovieDetailsUiEvent {
    data class OnCastAndCrewBottomSheetOpenChange(val open: Boolean) : MovieDetailsUiEvent()

    data object OnRetry : MovieDetailsUiEvent()
}
