package instamovies.app.data.remote.dto.series.details

import com.google.gson.annotations.SerializedName

data class CreatedBy(
    @SerializedName("credit_id")
    val creditId: String? = null,
    val gender: Int? = null,
    val id: Int? = null,
    val name: String? = null,
    @SerializedName("profile_path")
    val profilePath: String? = null,
)
