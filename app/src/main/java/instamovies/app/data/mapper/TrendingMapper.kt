package instamovies.app.data.mapper

import instamovies.app.data.remote.dto.trending.TrendingResult
import instamovies.app.data.remote.dto.trending.person.TrendingPersonResult
import instamovies.app.domain.model.trending.TrendingPersonResultModel
import instamovies.app.domain.model.trending.TrendingResultModel

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
