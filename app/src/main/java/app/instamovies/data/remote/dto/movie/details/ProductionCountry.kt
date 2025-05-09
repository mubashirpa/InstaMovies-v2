package app.instamovies.data.remote.dto.movie.details

import com.google.gson.annotations.SerializedName

data class ProductionCountry(
    @SerializedName("iso_3166_1")
    val iso31661: String? = null,
    val name: String? = null,
)
