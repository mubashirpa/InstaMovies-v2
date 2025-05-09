package app.instamovies.data.mapper

import app.instamovies.data.remote.dto.search.SearchResult
import app.instamovies.domain.model.search.SearchResultModel

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
