package app.instamovies.presentation.home

sealed class HomeUiEvent {
    data object OnRetry : HomeUiEvent()
}
