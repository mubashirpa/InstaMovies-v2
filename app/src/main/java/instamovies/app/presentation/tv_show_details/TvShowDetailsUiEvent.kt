package instamovies.app.presentation.tv_show_details

sealed class TvShowDetailsUiEvent {
    data class OnCastAndCrewBottomSheetOpenChange(val open: Boolean) : TvShowDetailsUiEvent()

    data class OnSeasonsBottomSheetOpenChange(val open: Boolean) : TvShowDetailsUiEvent()

    data object OnRetry : TvShowDetailsUiEvent()
}
