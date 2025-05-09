package app.instamovies.domain.usecase.preferences

import app.instamovies.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class GetUserPreferencesUseCase
    @Inject
    constructor(
        private val userPreferencesRepository: UserPreferencesRepository,
    ) {
        operator fun invoke() = userPreferencesRepository.userPreferencesFlow
    }
