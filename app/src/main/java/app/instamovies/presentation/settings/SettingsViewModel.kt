package app.instamovies.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import app.instamovies.domain.model.preferences.AppTheme
import app.instamovies.domain.usecase.preferences.GetUserPreferencesUseCase
import app.instamovies.domain.usecase.preferences.UpdateAppThemeUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(
        private val getUserPreferencesUseCase: GetUserPreferencesUseCase,
        private val updateAppThemeUseCase: UpdateAppThemeUseCase,
    ) : ViewModel() {
        var uiState by mutableStateOf(SettingsUiState())
            private set

        init {
            getUserPreferences()
        }

        fun onEvent(event: SettingsUiEvent) {
            when (event) {
                is SettingsUiEvent.OnAppThemeChange -> {
                    updateAppTheme(event.appTheme)
                }
            }
        }

        private fun getUserPreferences() {
            viewModelScope.launch {
                getUserPreferencesUseCase().collect { userPreferences ->
                    uiState = uiState.copy(selectedTheme = userPreferences.appTheme)
                }
            }
        }

        private fun updateAppTheme(appTheme: AppTheme) {
            viewModelScope.launch {
                updateAppThemeUseCase(appTheme)
            }
        }
    }
