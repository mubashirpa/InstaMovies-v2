package instamovies.app.domain.model.series.details

import instamovies.app.domain.model.series.SeriesResultModel
import instamovies.app.domain.model.series.content_rating.ContentRatingModel
import instamovies.app.domain.model.series.credits.SeriesCredits

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
