package instamovies.app.presentation.homeContainer

import androidx.compose.foundation.text.input.TextFieldState
import instamovies.app.core.util.Resource
import instamovies.app.domain.model.search.SearchResultModel
import instamovies.app.domain.model.trending.TrendingResultModel

data class HomeContainerUiState(
    val searchResource: Resource<List<SearchResultModel>> = Resource.Empty(),
    val searchQueryState: TextFieldState = TextFieldState(),
    val searching: Boolean = false,
    val trendingResource: Resource<List<TrendingResultModel>> = Resource.Empty(),
)
