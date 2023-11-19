package instamovies.app.data.remote.dto.person.popular

import com.google.gson.annotations.SerializedName

data class PersonResult(
    val adult: Boolean? = null,
    val gender: Int? = null,
    val id: Int? = null,
    @SerializedName("known_for")
    val knownFor: List<KnownFor>? = null,
    @SerializedName("known_for_department")
    val knownForDepartment: String? = null,
    val name: String? = null,
    val popularity: Double? = null,
    @SerializedName("profile_path")
    val profilePath: String? = null,
)
