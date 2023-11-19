package instamovies.app.data.remote.dto.series.details

import com.google.gson.annotations.SerializedName

data class SpokenLanguage(
    @SerializedName("english_name")
    val englishName: String? = null,
    @SerializedName("iso_639_1")
    val iso6391: String? = null,
    val name: String? = null,
)
