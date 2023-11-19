package instamovies.app.data.remote.dto.movie

import com.google.gson.annotations.SerializedName

data class MovieListsDto(
    val page: Int? = null,
    val results: List<MovieResult>? = null,
    @SerializedName("total_pages")
    val totalPages: Int? = null,
    @SerializedName("total_results")
    val totalResults: Int? = null,
)
