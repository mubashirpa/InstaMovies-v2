package app.instamovies.presentation.tvShowDetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import app.instamovies.domain.usecase.series.GetSeriesDetailsUseCase
import app.instamovies.navigation.Route
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TvShowDetailsViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val getSeriesDetailsUseCase: GetSeriesDetailsUseCase,
    ) : ViewModel() {
        var uiState by mutableStateOf(TvShowDetailsUiState())
            private set

        private val seriesId: Int = savedStateHandle.toRoute<Route.TvShowDetails>().seriesId

        init {
            val locale = Locale.getDefault()
            uiState = uiState.copy(userCountry = locale.country)
            getSeriesDetails(seriesId)
        }

        fun onEvent(event: TvShowDetailsUiEvent) {
            when (event) {
                is TvShowDetailsUiEvent.OnCastAndCrewBottomSheetOpenChange -> {
                    uiState = uiState.copy(openCastAndCrewBottomSheet = event.open)
                }

                is TvShowDetailsUiEvent.OnSeasonsBottomSheetOpenChange -> {
                    uiState = uiState.copy(openSeasonsBottomSheet = event.open)
                }

                TvShowDetailsUiEvent.OnRetry -> {
                    getSeriesDetails(seriesId)
                }
            }
        }

        private fun getSeriesDetails(seriesId: Int) {
            getSeriesDetailsUseCase(
                seriesId,
                appendToResponse = "content_ratings,credits,recommendations",
            ).onEach { resource ->
                uiState = uiState.copy(tvShowDetailsResource = resource)
            }.launchIn(viewModelScope)
        }
    }
