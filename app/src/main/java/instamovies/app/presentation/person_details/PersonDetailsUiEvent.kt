package instamovies.app.presentation.person_details

sealed class PersonDetailsUiEvent {
    data object OnRetry : PersonDetailsUiEvent()
}
