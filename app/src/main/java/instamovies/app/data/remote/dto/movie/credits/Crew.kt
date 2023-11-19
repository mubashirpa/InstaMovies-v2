package instamovies.app.data.remote.dto.movie.credits

import com.google.gson.annotations.SerializedName

data class Crew(
    val adult: Boolean? = null,
    @SerializedName("credit_id")
    val creditId: String? = null,
    val department: String? = null,
    val gender: Int? = null,
    val id: Int? = null,
    val job: String? = null,
    @SerializedName("known_for_department")
    val knownForDepartment: String? = null,
    val name: String? = null,
    @SerializedName("original_name")
    val originalName: String? = null,
    val popularity: Double? = null,
    @SerializedName("profile_path")
    val profilePath: String? = null,
)
