package instamovies.app.presentation.settings

import instamovies.app.domain.model.preferences.AppTheme

data class SettingsUiState(
    val selectedTheme: AppTheme = AppTheme.SYSTEM_DEFAULT,
)
