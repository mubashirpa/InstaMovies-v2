package app.instamovies.presentation.home

import app.instamovies.core.util.Resource
import app.instamovies.domain.model.list.ListResultModel
import app.instamovies.domain.model.movie.MovieResultModel
import app.instamovies.domain.model.trending.TrendingPersonResultModel

data class HomeUiState(
    val exploreResource: Resource<List<ListResultModel>> = Resource.Empty(),
    val popularMoviesResource: Resource<List<MovieResultModel>> = Resource.Empty(),
    val trendingPersonResource: Resource<List<TrendingPersonResultModel>> = Resource.Empty(),
)
