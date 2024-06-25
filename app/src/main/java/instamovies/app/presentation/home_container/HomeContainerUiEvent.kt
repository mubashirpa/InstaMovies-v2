package instamovies.app.presentation.home_container

sealed class HomeContainerUiEvent {
    data class OnQueryChange(val query: String) : HomeContainerUiEvent()

    data class OnSearchBarExpandedChange(val expanded: Boolean) : HomeContainerUiEvent()

    data object OnRetry : HomeContainerUiEvent()

    data object OnSearch : HomeContainerUiEvent()
}
