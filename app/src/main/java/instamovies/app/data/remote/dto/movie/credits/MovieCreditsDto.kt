package instamovies.app.data.remote.dto.movie.credits

data class MovieCreditsDto(
    val cast: List<Cast>? = null,
    val crew: List<Crew>? = null,
    val id: Int? = null,
)
