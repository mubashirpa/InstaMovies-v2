package instamovies.app.data.remote.dto.person.images

import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("aspect_ratio")
    val aspectRatio: Double? = null,
    @SerializedName("file_path")
    val filePath: String? = null,
    val height: Int? = null,
    @SerializedName("iso_639_1")
    val iso6391: Any? = null,
    @SerializedName("vote_average")
    val voteAverage: Double? = null,
    @SerializedName("vote_count")
    val voteCount: Int? = null,
    val width: Int? = null,
)
