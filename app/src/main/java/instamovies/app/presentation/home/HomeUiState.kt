package instamovies.app.presentation.home

import instamovies.app.core.util.Resource
import instamovies.app.domain.model.list.ListResultModel
import instamovies.app.domain.model.movie.MovieResultModel
import instamovies.app.domain.model.trending.TrendingPersonResultModel

data class HomeUiState(
    val exploreResource: Resource<List<ListResultModel>> = Resource.Empty(),
    val popularMoviesResource: Resource<List<MovieResultModel>> = Resource.Empty(),
    val trendingPersonResource: Resource<List<TrendingPersonResultModel>> = Resource.Empty(),
)
