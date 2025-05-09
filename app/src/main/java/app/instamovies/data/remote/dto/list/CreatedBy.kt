package app.instamovies.data.remote.dto.list

import com.google.gson.annotations.SerializedName

data class CreatedBy(
    @SerializedName("gravatar_hash")
    val gravatarHash: String? = null,
    val id: String? = null,
    val name: String? = null,
    val username: String? = null,
)
