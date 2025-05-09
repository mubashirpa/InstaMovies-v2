package app.instamovies.presentation.homeContainer

sealed class HomeContainerUiEvent {
    data object Retry : HomeContainerUiEvent()
}
