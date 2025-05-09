package app.instamovies.data.remote.dto.person.popular

import com.google.gson.annotations.SerializedName

data class PopularPersonDto(
    val page: Int? = null,
    val results: List<PersonResult>? = null,
    @SerializedName("total_pages")
    val totalPages: Int? = null,
    @SerializedName("total_results")
    val totalResults: Int? = null,
)
