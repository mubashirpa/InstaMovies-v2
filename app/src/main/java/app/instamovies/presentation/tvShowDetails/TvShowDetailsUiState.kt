package app.instamovies.presentation.tvShowDetails

import app.instamovies.core.util.Resource
import app.instamovies.domain.model.series.details.SeriesDetails

data class TvShowDetailsUiState(
    val openCastAndCrewBottomSheet: Boolean = false,
    val openSeasonsBottomSheet: Boolean = false,
    val tvShowDetailsResource: Resource<SeriesDetails> = Resource.Empty(),
    val userCountry: String = "",
)
