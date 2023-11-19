package instamovies.app.domain.model.search

import instamovies.app.data.remote.dto.search.KnownFor

data class SearchResultModel(
    val firstAirDate: String? = null,
    val genreIds: List<Int>? = null,
    val id: Int? = null,
    val knownFor: List<KnownFor>? = null,
    val knownForDepartment: String? = null,
    val mediaType: String? = null,
    val name: String? = null,
    val posterPath: String? = null,
    val profilePath: String? = null,
    val releaseDate: String? = null,
    val title: String? = null,
    val voteAverage: Double? = null,
)
