package instamovies.app.domain.usecase.preferences

import instamovies.app.domain.model.preferences.AppTheme
import instamovies.app.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class UpdateAppThemeUseCase
    @Inject
    constructor(
        private val userPreferencesRepository: UserPreferencesRepository,
    ) {
        suspend operator fun invoke(appTheme: AppTheme) = userPreferencesRepository.updateAppTheme(appTheme)
    }
