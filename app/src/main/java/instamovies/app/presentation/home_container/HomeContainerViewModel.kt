package instamovies.app.presentation.home_container

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import instamovies.app.core.util.Resource
import instamovies.app.domain.usecase.search.SearchMultiUseCase
import instamovies.app.domain.usecase.trending.GetTrendingUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject
import kotlin.concurrent.schedule

@HiltViewModel
class HomeContainerViewModel
    @Inject
    constructor(
        private val searchMultiUseCase: SearchMultiUseCase,
        private val getTrendingUseCase: GetTrendingUseCase,
    ) : ViewModel() {
        var uiState by mutableStateOf(HomeContainerUiState())
            private set

        private var searchMultiUseCaseJob: Job? = null
        private var timerTask: TimerTask? = null

        init {
            getTrending()
        }

        fun onEvent(event: HomeContainerUiEvent) {
            when (event) {
                is HomeContainerUiEvent.OnSearchBarActiveChange -> {
                    uiState = uiState.copy(searchbarActive = event.active)
                }

                is HomeContainerUiEvent.OnSearchTextChange -> {
                    uiState = uiState.copy(searchText = event.text)
                    searchMulti(event.text)
                }

                HomeContainerUiEvent.OnSearch -> {
                    uiState = uiState.copy(searchText = "", searchbarActive = false)
                }

                HomeContainerUiEvent.OnRetry -> {
                    if (uiState.trendingResource is Resource.Error) {
                        getTrending()
                    }
                }
            }
        }

        private fun getTrending() {
            getTrendingUseCase().onEach { resource ->
                uiState =
                    uiState.copy(searching = resource is Resource.Loading, trendingResource = resource)
            }.launchIn(viewModelScope)
        }

        private fun searchMulti(query: String) {
            timerTask?.cancel()
            timerTask = null
            searchMultiUseCaseJob?.cancel()
            searchMultiUseCaseJob = null
            uiState = uiState.copy(searching = false)
            if (query.isBlank()) {
                uiState = uiState.copy(searchResource = Resource.Empty())
                return
            }
            timerTask =
                Timer().schedule(500) {
                    searchMultiUseCaseJob =
                        searchMultiUseCase(query).onEach { resource ->
                            uiState =
                                uiState.copy(
                                    searchResource = resource,
                                    searching = resource is Resource.Loading,
                                )
                        }.launchIn(viewModelScope)
                }
        }
    }
