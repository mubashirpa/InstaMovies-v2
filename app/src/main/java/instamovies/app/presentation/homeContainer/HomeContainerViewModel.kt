package instamovies.app.presentation.homeContainer

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

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

        init {
            getTrending()
        }

        fun onEvent(event: HomeContainerUiEvent) {
            when (event) {
                is HomeContainerUiEvent.OnSearchBarExpandedChange -> {
                    uiState = uiState.copy(searchbarExpanded = event.expanded)
                }

                is HomeContainerUiEvent.OnQueryChange -> {
                    uiState = uiState.copy(searchQuery = event.query)
                    searchMulti(event.query)
                }

                HomeContainerUiEvent.OnSearch -> {
                    uiState =
                        uiState.copy(
                            searchQuery = "",
                            searchbarExpanded = false,
                        )
                }

                HomeContainerUiEvent.OnRetry -> {
                    if (uiState.trendingResource is Resource.Error) {
                        getTrending()
                    }
                }
            }
        }

        private fun getTrending() {
            getTrendingUseCase()
                .onEach { resource ->
                    uiState =
                        uiState.copy(
                            searching = resource is Resource.Loading,
                            trendingResource = resource,
                        )
                }.launchIn(viewModelScope)
        }

        private fun searchMulti(query: String) {
            searchMultiUseCaseJob?.cancel()
            searchMultiUseCaseJob = null
            uiState = uiState.copy(searching = false)
            if (query.isBlank()) {
                uiState = uiState.copy(searchResource = Resource.Empty())
                return
            }
            searchMultiUseCaseJob =
                viewModelScope.launch {
                    delay(500)
                    searchMultiUseCase(query)
                        .onEach { resource ->
                            uiState =
                                uiState.copy(
                                    searchResource = resource,
                                    searching = resource is Resource.Loading,
                                )
                        }.launchIn(this)
                }
        }
    }
