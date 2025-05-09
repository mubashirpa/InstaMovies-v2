package app.instamovies.presentation.personDetails

sealed class PersonDetailsUiEvent {
    data object OnRetry : PersonDetailsUiEvent()
}
