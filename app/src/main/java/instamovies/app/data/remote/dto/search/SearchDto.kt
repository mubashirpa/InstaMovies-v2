package instamovies.app.data.remote.dto.search

import com.google.gson.annotations.SerializedName

data class SearchDto(
    val page: Int? = null,
    val results: List<SearchResult>? = null,
    @SerializedName("total_pages")
    val totalPages: Int? = null,
    @SerializedName("total_results")
    val totalResults: Int? = null,
)
