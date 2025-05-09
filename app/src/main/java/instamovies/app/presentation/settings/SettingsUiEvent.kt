package instamovies.app.presentation.settings

import instamovies.app.domain.model.preferences.AppTheme

sealed class SettingsUiEvent {
    data class OnAppThemeChange(
        val appTheme: AppTheme,
    ) : SettingsUiEvent()
}
