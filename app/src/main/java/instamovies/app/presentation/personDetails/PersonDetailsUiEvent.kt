package instamovies.app.presentation.personDetails

sealed class PersonDetailsUiEvent {
    data object OnRetry : PersonDetailsUiEvent()
}
