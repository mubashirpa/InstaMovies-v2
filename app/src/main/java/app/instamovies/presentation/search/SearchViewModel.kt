package app.instamovies.presentation.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import app.instamovies.domain.model.search.SearchResultModel
import app.instamovies.domain.usecase.search.SearchMultiPagingUseCase
import app.instamovies.navigation.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val searchMultiPagingUseCase: SearchMultiPagingUseCase,
    ) : ViewModel() {
        private val searchQuery: String = savedStateHandle.toRoute<Route.Search>().searchQuery

        private val _searchStateFlow: MutableStateFlow<PagingData<SearchResultModel>> =
            MutableStateFlow(PagingData.empty())
        val searchStateFlow: MutableStateFlow<PagingData<SearchResultModel>> get() = _searchStateFlow

        init {
            searchMulti(searchQuery)
        }

        private fun searchMulti(query: String) {
            viewModelScope.launch {
                searchMultiPagingUseCase(query)
                    .distinctUntilChanged()
                    .cachedIn(viewModelScope)
                    .collect {
                        _searchStateFlow.value = it
                    }
            }
        }
    }
