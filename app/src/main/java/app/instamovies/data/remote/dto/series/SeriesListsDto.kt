package app.instamovies.data.remote.dto.series

import com.google.gson.annotations.SerializedName

data class SeriesListsDto(
    val page: Int? = null,
    val results: List<SeriesResult>? = null,
    @SerializedName("total_pages")
    val totalPages: Int? = null,
    @SerializedName("total_results")
    val totalResults: Int? = null,
)
