package instamovies.app.presentation.person_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import instamovies.app.domain.usecase.person.GetPersonDetailsUseCase
import instamovies.app.navigation.Routes
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PersonDetailsViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val getPersonDetailsUseCase: GetPersonDetailsUseCase,
    ) : ViewModel() {
        var uiState by mutableStateOf(PersonDetailsUiState())
            private set

        private val personId: Int =
            checkNotNull(savedStateHandle[Routes.Args.PERSON_DETAILS_PERSON_ID])

        init {
            getPersonDetails(personId)
        }

        fun onEvent(event: PersonDetailsUiEvent) {
            when (event) {
                PersonDetailsUiEvent.OnRetry -> {
                    getPersonDetails(personId)
                }
            }
        }

        private fun getPersonDetails(id: Int) {
            getPersonDetailsUseCase(
                personId = id,
                appendToResponse = "combined_credits,images",
            ).onEach { resource ->
                uiState = uiState.copy(personDetailsResource = resource)
            }.launchIn(viewModelScope)
        }
    }
