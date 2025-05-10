package app.instamovies.presentation.main

import com.google.android.play.core.appupdate.AppUpdateInfo

data class MainUiState(
    val updateAvailable: Boolean = false,
    val updateInfo: AppUpdateInfo? = null,
)
