package instamovies.app.presentation.home

sealed class HomeUiEvent {
    data object OnRetry : HomeUiEvent()
}
