package instamovies.app.data.remote.dto.trending.person

import com.google.gson.annotations.SerializedName

data class TrendingPersonDto(
    val page: Int? = null,
    val results: List<TrendingPersonResult>? = null,
    @SerializedName("total_pages")
    val totalPages: Int? = null,
    @SerializedName("total_results")
    val totalResults: Int? = null,
)
