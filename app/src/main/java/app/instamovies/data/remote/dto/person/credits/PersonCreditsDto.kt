package app.instamovies.data.remote.dto.person.credits

data class PersonCreditsDto(
    val cast: List<Cast>? = null,
    val crew: List<Crew>? = null,
    val id: Int? = null,
)
