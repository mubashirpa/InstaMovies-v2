package instamovies.app.presentation.personDetails

import instamovies.app.core.util.Resource
import instamovies.app.domain.model.person.PersonDetails

data class PersonDetailsUiState(
    val personDetailsResource: Resource<PersonDetails> = Resource.Empty(),
)
