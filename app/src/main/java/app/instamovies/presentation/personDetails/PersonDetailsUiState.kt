package app.instamovies.presentation.personDetails

import app.instamovies.core.util.Resource
import app.instamovies.domain.model.person.PersonDetails

data class PersonDetailsUiState(
    val personDetailsResource: Resource<PersonDetails> = Resource.Empty(),
)
