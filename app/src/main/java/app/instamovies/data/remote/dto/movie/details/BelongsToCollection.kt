package app.instamovies.data.remote.dto.movie.details

import com.google.gson.annotations.SerializedName

data class BelongsToCollection(
    @SerializedName("backdrop_path")
    val backdropPath: String? = null,
    val id: Int? = null,
    val name: String? = null,
    @SerializedName("poster_path")
    val posterPath: String? = null,
)
