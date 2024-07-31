package instamovies.app.data.remote.dto.series.details

import com.google.gson.annotations.SerializedName
import instamovies.app.data.remote.dto.series.SeriesListsDto
import instamovies.app.data.remote.dto.series.contentRating.ContentRating
import instamovies.app.data.remote.dto.series.credits.SeriesCreditsDto

data class SeriesDetailsDto(
    val adult: Boolean? = null,
    @SerializedName("backdrop_path")
    val backdropPath: String? = null,
    @SerializedName("created_by")
    val createdBy: List<CreatedBy>? = null,
    @SerializedName("episode_run_time")
    val episodeRunTime: List<Int>? = null,
    @SerializedName("first_air_date")
    val firstAirDate: String? = null,
    val genres: List<Genre>? = null,
    val homepage: String? = null,
    val id: Int? = null,
    @SerializedName("in_production")
    val inProduction: Boolean? = null,
    val languages: List<String>? = null,
    @SerializedName("last_air_date")
    val lastAirDate: String? = null,
    @SerializedName("last_episode_to_air")
    val lastEpisodeToAir: EpisodeToAir? = null,
    val name: String? = null,
    val networks: List<Network>? = null,
    @SerializedName("next_episode_to_air")
    val nextEpisodeToAir: EpisodeToAir? = null,
    @SerializedName("number_of_episodes")
    val numberOfEpisodes: Int? = null,
    @SerializedName("number_of_seasons")
    val numberOfSeasons: Int? = null,
    @SerializedName("origin_country")
    val originCountry: List<String>? = null,
    @SerializedName("original_language")
    val originalLanguage: String? = null,
    @SerializedName("original_name")
    val originalName: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    @SerializedName("poster_path")
    val posterPath: String? = null,
    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompany>? = null,
    @SerializedName("production_countries")
    val productionCountries: List<ProductionCountry>? = null,
    val seasons: List<Season>? = null,
    @SerializedName("spoken_languages")
    val spokenLanguages: List<SpokenLanguage>? = null,
    val status: String? = null,
    val tagline: String? = null,
    val type: String? = null,
    @SerializedName("vote_average")
    val voteAverage: Double? = null,
    @SerializedName("vote_count")
    val voteCount: Int? = null,
    // AppendToResponse
    @SerializedName("content_ratings")
    val contentRatings: ContentRating? = null,
    val credits: SeriesCreditsDto? = null,
    val recommendations: SeriesListsDto? = null,
)
