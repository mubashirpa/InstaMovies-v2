package instamovies.app.presentation.homeContainer

sealed class HomeContainerUiEvent {
    data object Retry : HomeContainerUiEvent()
}
