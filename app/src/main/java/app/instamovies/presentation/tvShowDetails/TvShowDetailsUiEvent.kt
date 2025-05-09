package app.instamovies.presentation.tvShowDetails

sealed class TvShowDetailsUiEvent {
    data class OnCastAndCrewBottomSheetOpenChange(
        val open: Boolean,
    ) : TvShowDetailsUiEvent()

    data class OnSeasonsBottomSheetOpenChange(
        val open: Boolean,
    ) : TvShowDetailsUiEvent()

    data object OnRetry : TvShowDetailsUiEvent()
}
