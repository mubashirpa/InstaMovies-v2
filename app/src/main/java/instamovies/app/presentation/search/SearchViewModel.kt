package instamovies.app.presentation.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import instamovies.app.domain.model.search.SearchResultModel
import instamovies.app.domain.usecase.search.SearchMultiPagingUseCase
import instamovies.app.navigation.Screen
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
        private val searchQuery: String = savedStateHandle.toRoute<Screen.Search>().searchQuery

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
