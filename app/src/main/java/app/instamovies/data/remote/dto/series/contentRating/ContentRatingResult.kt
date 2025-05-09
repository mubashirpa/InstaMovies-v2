package app.instamovies.data.remote.dto.series.contentRating

import com.google.gson.annotations.SerializedName

data class ContentRatingResult(
    val descriptors: List<String>? = null,
    @SerializedName("iso_3166_1")
    val iso31661: String? = null,
    val rating: String? = null,
)
