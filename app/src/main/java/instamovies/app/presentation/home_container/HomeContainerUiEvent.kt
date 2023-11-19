package instamovies.app.presentation.home_container

sealed class HomeContainerUiEvent {
    data class OnSearchTextChange(val text: String) : HomeContainerUiEvent()

    data class OnSearchBarActiveChange(val active: Boolean) : HomeContainerUiEvent()

    data object OnSearch : HomeContainerUiEvent()

    data object OnRetry : HomeContainerUiEvent()
}
