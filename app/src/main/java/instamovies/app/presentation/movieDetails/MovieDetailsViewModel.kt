package instamovies.app.presentation.movieDetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import instamovies.app.domain.usecase.movie.GetMovieDetailsUseCase
import instamovies.app.navigation.Screen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    ) : ViewModel() {
        var uiState by mutableStateOf(MovieDetailsUiState())
            private set

        private val movieId: Int = savedStateHandle.toRoute<Screen.MovieDetails>().movieId

        init {
            getMovieDetails(movieId)
        }

        fun onEvent(event: MovieDetailsUiEvent) {
            when (event) {
                is MovieDetailsUiEvent.OnCastAndCrewBottomSheetOpenChange -> {
                    uiState = uiState.copy(openCastAndCrewBottomSheet = event.open)
                }

                MovieDetailsUiEvent.OnRetry -> {
                    getMovieDetails(movieId)
                }
            }
        }

        private fun getMovieDetails(id: Int) {
            getMovieDetailsUseCase(
                movieId = id,
                appendToResponse = "credits,recommendations",
            ).onEach { resource ->
                uiState = uiState.copy(movieDetailsResource = resource)
            }.launchIn(viewModelScope)
        }
    }
