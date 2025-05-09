package app.instamovies.presentation.homeContainer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import app.instamovies.core.util.Resource
import app.instamovies.domain.usecase.search.SearchMultiUseCase
import app.instamovies.domain.usecase.trending.GetTrendingUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
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
            collectSearchQuery()
        }

        fun onEvent(event: HomeContainerUiEvent) {
            when (event) {
                HomeContainerUiEvent.Retry -> {
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
                searchMultiUseCase(query)
                    .onEach { resource ->
                        uiState =
                            uiState.copy(
                                searchResource = resource,
                                searching = resource is Resource.Loading,
                            )
                    }.launchIn(viewModelScope)
        }

        @OptIn(FlowPreview::class)
        private fun collectSearchQuery() {
            viewModelScope.launch {
                snapshotFlow { uiState.searchQueryState.text }
                    .debounce(500)
                    .collectLatest { query ->
                        searchMulti(query.trim().toString())
                    }
            }
        }
    }
