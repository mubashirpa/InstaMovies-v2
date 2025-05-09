package app.instamovies.domain.model.series.details

import app.instamovies.domain.model.series.SeriesResultModel
import app.instamovies.domain.model.series.contentRating.ContentRatingModel
import app.instamovies.domain.model.series.credits.SeriesCredits

data class SeriesDetails(
    val backdropPath: String? = null,
    val createdBy: List<SeriesCreatedBy>? = null,
    val firstAirDate: String? = null,
    val genres: List<SeriesGenre>? = null,
    val id: Int? = null,
    val name: String? = null,
    val numberOfEpisodes: Int? = null,
    val numberOfSeasons: Int? = null,
    val overview: String? = null,
    val seasons: List<SeriesSeason>? = null,
    val voteAverage: Double? = null,
    val contentRatings: ContentRatingModel? = null,
    val credits: SeriesCredits? = null,
    val recommendations: List<SeriesResultModel>? = null,
)
