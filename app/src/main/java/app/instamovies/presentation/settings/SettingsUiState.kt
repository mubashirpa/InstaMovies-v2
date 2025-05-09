package app.instamovies.presentation.settings

import app.instamovies.domain.model.preferences.AppTheme

data class SettingsUiState(
    val selectedTheme: AppTheme = AppTheme.SYSTEM_DEFAULT,
)
