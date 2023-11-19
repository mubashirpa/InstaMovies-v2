package instamovies.app.data.remote.dto.series.credits

data class SeriesCreditsDto(
    val cast: List<Cast>? = null,
    val crew: List<Crew>? = null,
    val id: Int? = null,
)
