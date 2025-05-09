package app.instamovies.presentation.settings

import app.instamovies.domain.model.preferences.AppTheme

sealed class SettingsUiEvent {
    data class OnAppThemeChange(
        val appTheme: AppTheme,
    ) : SettingsUiEvent()
}
