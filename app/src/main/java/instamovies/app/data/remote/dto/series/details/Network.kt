package instamovies.app.data.remote.dto.series.details

import com.google.gson.annotations.SerializedName

data class Network(
    val id: Int? = null,
    @SerializedName("logo_path")
    val logoPath: String? = null,
    val name: String? = null,
    @SerializedName("origin_country")
    val originCountry: String? = null,
)
