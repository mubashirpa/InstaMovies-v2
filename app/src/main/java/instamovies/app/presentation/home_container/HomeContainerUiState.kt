package instamovies.app.presentation.home_container

import instamovies.app.core.util.Resource
import instamovies.app.domain.model.search.SearchResultModel
import instamovies.app.domain.model.trending.TrendingResultModel

data class HomeContainerUiState(
    val searchQuery: String = "",
    val searchResource: Resource<List<SearchResultModel>> = Resource.Empty(),
    val searchbarExpanded: Boolean = false,
    val searching: Boolean = false,
    val trendingResource: Resource<List<TrendingResultModel>> = Resource.Empty(),
)
