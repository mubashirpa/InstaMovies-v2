package app.instamovies.data.mapper

import app.instamovies.data.remote.dto.trending.TrendingResult
import app.instamovies.data.remote.dto.trending.person.TrendingPersonResult
import app.instamovies.domain.model.trending.TrendingPersonResultModel
import app.instamovies.domain.model.trending.TrendingResultModel

fun TrendingResult.toTrendingResultModel(): TrendingResultModel {
    return TrendingResultModel(
        firstAirDate,
        id,
        mediaType,
        name,
        posterPath,
        releaseDate,
        title,
        voteAverage,
    )
}

fun TrendingPersonResult.toTrendingPersonResultModel(): TrendingPersonResultModel {
    return TrendingPersonResultModel(id, knownForDepartment, name, profilePath)
}
