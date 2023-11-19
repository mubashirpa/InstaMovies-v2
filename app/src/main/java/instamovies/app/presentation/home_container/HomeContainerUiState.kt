package instamovies.app.presentation.home_container

import instamovies.app.core.util.Resource
import instamovies.app.domain.model.search.SearchResultModel
import instamovies.app.domain.model.trending.TrendingResultModel

data class HomeContainerUiState(
    val searchResource: Resource<List<SearchResultModel>> = Resource.Empty(),
    val searchText: String = "",
    val searchbarActive: Boolean = false,
    val searching: Boolean = false,
    val trendingResource: Resource<List<TrendingResultModel>> = Resource.Empty(),
)
