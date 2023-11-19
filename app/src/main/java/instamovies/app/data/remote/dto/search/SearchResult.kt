package instamovies.app.data.remote.dto.search

import com.google.gson.annotations.SerializedName

data class SearchResult(
    val adult: Boolean? = null,
    @SerializedName("backdrop_path")
    val backdropPath: String? = null,
    @SerializedName("first_air_date")
    val firstAirDate: String? = null,
    val gender: Int? = null,
    @SerializedName("genre_ids")
    val genreIds: List<Int>? = null,
    val id: Int? = null,
    @SerializedName("known_for")
    val knownFor: List<KnownFor>? = null,
    @SerializedName("known_for_department")
    val knownForDepartment: String? = null,
    @SerializedName("media_type")
    val mediaType: String? = null,
    val name: String? = null,
    @SerializedName("origin_country")
    val originCountry: List<String>? = null,
    @SerializedName("original_language")
    val originalLanguage: String? = null,
    @SerializedName("original_name")
    val originalName: String? = null,
    @SerializedName("original_title")
    val originalTitle: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    @SerializedName("poster_path")
    val posterPath: String? = null,
    @SerializedName("profile_path")
    val profilePath: String? = null,
    @SerializedName("release_date")
    val releaseDate: String? = null,
    val title: String? = null,
    val video: Boolean? = null,
    @SerializedName("vote_average")
    val voteAverage: Double? = null,
    @SerializedName("vote_count")
    val voteCount: Int? = null,
)
