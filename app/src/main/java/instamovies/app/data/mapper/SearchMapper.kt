package instamovies.app.data.mapper

import instamovies.app.data.remote.dto.search.SearchResult
import instamovies.app.domain.model.search.SearchResultModel

fun SearchResult.toSearchResultModel(): SearchResultModel {
    return SearchResultModel(
        firstAirDate,
        genreIds,
        id,
        knownFor,
        knownForDepartment,
        mediaType,
        name,
        posterPath,
        profilePath,
        releaseDate,
        title,
        voteAverage,
    )
}
