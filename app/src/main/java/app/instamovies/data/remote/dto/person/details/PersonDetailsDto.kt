package app.instamovies.data.remote.dto.person.details

import com.google.gson.annotations.SerializedName
import app.instamovies.data.remote.dto.person.credits.PersonCreditsDto
import app.instamovies.data.remote.dto.person.images.PersonImagesDto

data class PersonDetailsDto(
    val adult: Boolean? = null,
    @SerializedName("also_known_as")
    val alsoKnownAs: List<String>? = null,
    val biography: String? = null,
    val birthday: String? = null,
    val deathday: String? = null,
    val gender: Int? = null,
    val homepage: Any? = null,
    val id: Int? = null,
    @SerializedName("imdb_id")
    val imdbId: String? = null,
    @SerializedName("known_for_department")
    val knownForDepartment: String? = null,
    val name: String? = null,
    @SerializedName("place_of_birth")
    val placeOfBirth: String? = null,
    val popularity: Double? = null,
    @SerializedName("profile_path")
    val profilePath: String? = null,
    // AppendToResponse
    @SerializedName("combined_credits")
    val credits: PersonCreditsDto? = null,
    val images: PersonImagesDto? = null,
)
