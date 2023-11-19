package instamovies.app.data.remote.dto.series.credits

import com.google.gson.annotations.SerializedName

data class Cast(
    val adult: Boolean? = null,
    val character: String? = null,
    @SerializedName("credit_id")
    val creditId: String? = null,
    val gender: Int? = null,
    val id: Int? = null,
    @SerializedName("known_for_department")
    val knownForDepartment: String? = null,
    val name: String? = null,
    val order: Int? = null,
    @SerializedName("original_name")
    val originalName: String? = null,
    val popularity: Double? = null,
    @SerializedName("profile_path")
    val profilePath: String? = null,
)
