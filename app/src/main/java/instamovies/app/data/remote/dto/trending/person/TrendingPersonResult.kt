package instamovies.app.data.remote.dto.trending.person

import com.google.gson.annotations.SerializedName

data class TrendingPersonResult(
    val adult: Boolean? = null,
    val gender: Int? = null,
    val id: Int? = null,
    @SerializedName("known_for")
    val knownFor: List<KnownFor>? = null,
    @SerializedName("known_for_department")
    val knownForDepartment: String? = null,
    @SerializedName("media_type")
    val mediaType: String? = null,
    val name: String? = null,
    @SerializedName("original_name")
    val originalName: String? = null,
    val popularity: Double? = null,
    @SerializedName("profile_path")
    val profilePath: String? = null,
)
