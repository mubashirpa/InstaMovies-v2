package instamovies.app.presentation.tvShowDetails

import instamovies.app.core.util.Resource
import instamovies.app.domain.model.series.details.SeriesDetails

data class TvShowDetailsUiState(
    val openCastAndCrewBottomSheet: Boolean = false,
    val openSeasonsBottomSheet: Boolean = false,
    val tvShowDetailsResource: Resource<SeriesDetails> = Resource.Empty(),
    val userCountry: String = "",
)
