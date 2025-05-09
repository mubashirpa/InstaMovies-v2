package app.instamovies.data.remote.dto.list

import com.google.gson.annotations.SerializedName

data class ListsDto(
    @SerializedName("average_rating")
    val averageRating: Double? = null,
    @SerializedName("backdrop_path")
    val backdropPath: String? = null,
    val comments: Any? = null,
    @SerializedName("created_by")
    val createdBy: CreatedBy? = null,
    val description: String? = null,
    val id: Int? = null,
    @SerializedName("iso_3166_1")
    val iso31661: String? = null,
    @SerializedName("iso_639_1")
    val iso6391: String? = null,
    val name: String? = null,
    @SerializedName("object_ids")
    val objectIds: Any? = null,
    val page: Int? = null,
    @SerializedName("poster_path")
    val posterPath: String? = null,
    val `public`: Boolean? = null,
    val results: List<ListResult>? = null,
    val revenue: Long? = null,
    val runtime: Int? = null,
    @SerializedName("sort_by")
    val sortBy: String? = null,
    @SerializedName("total_pages")
    val totalPages: Int? = null,
    @SerializedName("total_results")
    val totalResults: Int? = null,
)
