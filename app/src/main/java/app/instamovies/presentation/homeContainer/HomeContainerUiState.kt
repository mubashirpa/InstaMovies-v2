package app.instamovies.presentation.homeContainer

import androidx.compose.foundation.text.input.TextFieldState
import app.instamovies.core.util.Resource
import app.instamovies.domain.model.search.SearchResultModel
import app.instamovies.domain.model.trending.TrendingResultModel

data class HomeContainerUiState(
    val searchResource: Resource<List<SearchResultModel>> = Resource.Empty(),
    val searchQueryState: TextFieldState = TextFieldState(),
    val searching: Boolean = false,
    val trendingResource: Resource<List<TrendingResultModel>> = Resource.Empty(),
)
