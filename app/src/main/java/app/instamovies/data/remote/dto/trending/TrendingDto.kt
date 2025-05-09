package app.instamovies.data.remote.dto.trending

import com.google.gson.annotations.SerializedName

data class TrendingDto(
    val page: Int? = null,
    val results: List<TrendingResult>? = null,
    @SerializedName("total_pages")
    val totalPages: Int? = null,
    @SerializedName("total_results")
    val totalResults: Int? = null,
)
