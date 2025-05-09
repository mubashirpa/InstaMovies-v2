package app.instamovies.data.mapper

import app.instamovies.data.remote.dto.series.SeriesResult
import app.instamovies.data.remote.dto.series.contentRating.ContentRating
import app.instamovies.data.remote.dto.series.contentRating.ContentRatingResult
import app.instamovies.data.remote.dto.series.credits.Cast
import app.instamovies.data.remote.dto.series.credits.Crew
import app.instamovies.data.remote.dto.series.credits.SeriesCreditsDto
import app.instamovies.data.remote.dto.series.details.CreatedBy
import app.instamovies.data.remote.dto.series.details.Genre
import app.instamovies.data.remote.dto.series.details.Season
import app.instamovies.data.remote.dto.series.details.SeriesDetailsDto
import app.instamovies.domain.model.series.SeriesResultModel
import app.instamovies.domain.model.series.contentRating.ContentRatingModel
import app.instamovies.domain.model.series.contentRating.ContentRatingResultModel
import app.instamovies.domain.model.series.credits.SeriesCast
import app.instamovies.domain.model.series.credits.SeriesCredits
import app.instamovies.domain.model.series.credits.SeriesCrew
import app.instamovies.domain.model.series.details.SeriesCreatedBy
import app.instamovies.domain.model.series.details.SeriesDetails
import app.instamovies.domain.model.series.details.SeriesGenre
import app.instamovies.domain.model.series.details.SeriesSeason

fun SeriesDetailsDto.toSeriesDetails(): SeriesDetails =
    SeriesDetails(
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

fun CreatedBy.toSeriesCreatedBy(): SeriesCreatedBy = SeriesCreatedBy(name)

fun Genre.toSeriesGenre(): SeriesGenre = SeriesGenre(name)

fun Season.toSeriesSeason(): SeriesSeason =
    SeriesSeason(
        airDate,
        episodeCount,
        id,
        name,
        overview,
        posterPath,
        seasonNumber,
        voteAverage,
    )

fun ContentRating.toContentRatingModel(): ContentRatingModel = ContentRatingModel(results?.map { it.toContentRatingResultModel() })

fun ContentRatingResult.toContentRatingResultModel(): ContentRatingResultModel = ContentRatingResultModel(iso31661, rating)

fun SeriesCreditsDto.toSeriesCredits(): SeriesCredits = SeriesCredits(cast?.map { it.toSeriesCast() }, crew?.map { it.toSeriesCrew() })

fun Cast.toSeriesCast(): SeriesCast = SeriesCast(character, id, name, profilePath)

fun Crew.toSeriesCrew(): SeriesCrew = SeriesCrew(id, job, name, profilePath)

fun SeriesResult.toSeriesResultModel(): SeriesResultModel = SeriesResultModel(firstAirDate, id, name, posterPath, voteAverage)
