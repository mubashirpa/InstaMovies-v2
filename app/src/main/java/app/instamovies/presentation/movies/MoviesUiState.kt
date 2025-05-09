package app.instamovies.presentation.movies

import androidx.paging.PagingData
import app.instamovies.domain.model.movie.MovieResultModel
import kotlinx.coroutines.flow.MutableStateFlow

data class MoviesUiState(
    val isNowPlayingMoviesLoaded: Boolean = false,
    val isTopRatedMoviesLoaded: Boolean = false,
    val isUpcomingMoviesLoaded: Boolean = false,
    val nowPlayingMovies: MutableStateFlow<PagingData<MovieResultModel>> =
        MutableStateFlow(
            PagingData.empty(),
        ),
    val popularMovies: MutableStateFlow<PagingData<MovieResultModel>> = MutableStateFlow(PagingData.empty()),
    val topRatedMovies: MutableStateFlow<PagingData<MovieResultModel>> =
        MutableStateFlow(
            PagingData.empty(),
        ),
    val upcomingMovies: MutableStateFlow<PagingData<MovieResultModel>> =
        MutableStateFlow(
            PagingData.empty(),
        ),
)
