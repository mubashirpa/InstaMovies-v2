package instamovies.app.data.mapper

import instamovies.app.data.remote.dto.series.SeriesResult
import instamovies.app.data.remote.dto.series.content_rating.ContentRating
import instamovies.app.data.remote.dto.series.content_rating.ContentRatingResult
import instamovies.app.data.remote.dto.series.credits.Cast
import instamovies.app.data.remote.dto.series.credits.Crew
import instamovies.app.data.remote.dto.series.credits.SeriesCreditsDto
import instamovies.app.data.remote.dto.series.details.CreatedBy
import instamovies.app.data.remote.dto.series.details.Genre
import instamovies.app.data.remote.dto.series.details.Season
import instamovies.app.data.remote.dto.series.details.SeriesDetailsDto
import instamovies.app.domain.model.series.SeriesResultModel
import instamovies.app.domain.model.series.content_rating.ContentRatingModel
import instamovies.app.domain.model.series.content_rating.ContentRatingResultModel
import instamovies.app.domain.model.series.credits.SeriesCast
import instamovies.app.domain.model.series.credits.SeriesCredits
import instamovies.app.domain.model.series.credits.SeriesCrew
import instamovies.app.domain.model.series.details.SeriesCreatedBy
import instamovies.app.domain.model.series.details.SeriesDetails
import instamovies.app.domain.model.series.details.SeriesGenre
import instamovies.app.domain.model.series.details.SeriesSeason

fun SeriesDetailsDto.toSeriesDetails(): SeriesDetails {
    return SeriesDetails(
        backdropPath,
        createdBy?.map { it.toSeriesCreatedBy() },
        firstAirDate,
        genres?.map { it.toSeriesGenre() },
        id,
        name,
        numberOfEpisodes,
        numberOfSeasons,
        overview,
        seasons?.map { it.toSeriesSeason() },
        voteAverage,
        contentRatings?.toContentRatingModel(),
        credits?.toSeriesCredits(),
        recommendations?.results?.map { it.toSeriesResultModel() },
    )
}

fun CreatedBy.toSeriesCreatedBy(): SeriesCreatedBy {
    return SeriesCreatedBy(name)
}

fun Genre.toSeriesGenre(): SeriesGenre {
    return SeriesGenre(name)
}

fun Season.toSeriesSeason(): SeriesSeason {
    return SeriesSeason(
        airDate,
        episodeCount,
        id,
        name,
        overview,
        posterPath,
        seasonNumber,
        voteAverage,
    )
}

fun ContentRating.toContentRatingModel(): ContentRatingModel {
    return ContentRatingModel(results?.map { it.toContentRatingResultModel() })
}

fun ContentRatingResult.toContentRatingResultModel(): ContentRatingResultModel {
    return ContentRatingResultModel(iso31661, rating)
}

fun SeriesCreditsDto.toSeriesCredits(): SeriesCredits {
    return SeriesCredits(cast?.map { it.toSeriesCast() }, crew?.map { it.toSeriesCrew() })
}

fun Cast.toSeriesCast(): SeriesCast {
    return SeriesCast(character, id, name, profilePath)
}

fun Crew.toSeriesCrew(): SeriesCrew {
    return SeriesCrew(id, job, name, profilePath)
}

fun SeriesResult.toSeriesResultModel(): SeriesResultModel {
    return SeriesResultModel(firstAirDate, id, name, posterPath, voteAverage)
}
