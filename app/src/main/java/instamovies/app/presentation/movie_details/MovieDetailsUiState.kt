package instamovies.app.presentation.movie_details

import instamovies.app.core.util.Resource
import instamovies.app.domain.model.movie.details.MovieDetails

data class MovieDetailsUiState(
    val movieDetailsResource: Resource<MovieDetails> = Resource.Empty(),
    val openCastAndCrewBottomSheet: Boolean = false,
)
