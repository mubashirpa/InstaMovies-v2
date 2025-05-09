package app.instamovies.domain.usecase.preferences

import app.instamovies.domain.model.preferences.AppTheme
import app.instamovies.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class UpdateAppThemeUseCase
    @Inject
    constructor(
        private val userPreferencesRepository: UserPreferencesRepository,
    ) {
        suspend operator fun invoke(appTheme: AppTheme) = userPreferencesRepository.updateAppTheme(appTheme)
    }
