package instamovies.app.domain.usecase.preferences

import instamovies.app.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class GetUserPreferencesUseCase
    @Inject
    constructor(
        private val userPreferencesRepository: UserPreferencesRepository,
    ) {
        operator fun invoke() = userPreferencesRepository.userPreferencesFlow
    }
