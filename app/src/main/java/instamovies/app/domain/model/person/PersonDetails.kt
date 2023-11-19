package instamovies.app.domain.model.person

import instamovies.app.domain.model.person.credits.PersonCredits
import instamovies.app.domain.model.person.images.PersonImages

data class PersonDetails(
    val biography: String? = null,
    val birthday: String? = null,
    val knownForDepartment: String? = null,
    val name: String? = null,
    val placeOfBirth: String? = null,
    val popularity: Double? = null,
    val profilePath: String? = null,
    val credits: PersonCredits? = null,
    val images: PersonImages? = null,
)
