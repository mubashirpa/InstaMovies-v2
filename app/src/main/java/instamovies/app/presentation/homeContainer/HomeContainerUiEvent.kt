package instamovies.app.presentation.homeContainer

sealed class HomeContainerUiEvent {
    data class OnQueryChange(
        val query: String,
    ) : HomeContainerUiEvent()

    data class OnSearchBarExpandedChange(
        val expanded: Boolean,
    ) : HomeContainerUiEvent()

    data object OnRetry : HomeContainerUiEvent()

    data object OnSearch : HomeContainerUiEvent()
}
