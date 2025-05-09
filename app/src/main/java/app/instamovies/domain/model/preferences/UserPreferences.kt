package app.instamovies.domain.model.preferences

data class UserPreferences(
    val appTheme: AppTheme = AppTheme.SYSTEM_DEFAULT,
)
