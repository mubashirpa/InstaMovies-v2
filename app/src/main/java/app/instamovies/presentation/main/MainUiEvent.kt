package app.instamovies.presentation.main

sealed class MainUiEvent {
    data object OnResume : MainUiEvent()
}
