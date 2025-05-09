package app.instamovies.presentation.movieDetails

import app.instamovies.core.util.Resource
import app.instamovies.domain.model.movie.details.MovieDetails

data class MovieDetailsUiState(
    val movieDetailsResource: Resource<MovieDetails> = Resource.Empty(),
    val openCastAndCrewBottomSheet: Boolean = false,
)
