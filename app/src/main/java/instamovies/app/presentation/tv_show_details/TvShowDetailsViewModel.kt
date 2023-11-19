package instamovies.app.presentation.tv_show_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import instamovies.app.domain.usecase.series.GetSeriesDetailsUseCase
import instamovies.app.navigation.Routes
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

        private val seriesId: Int =
            checkNotNull(savedStateHandle[Routes.Args.TV_SHOW_DETAILS_SERIES_ID])

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
