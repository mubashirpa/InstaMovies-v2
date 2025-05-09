package app.instamovies.domain.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import app.instamovies.domain.model.preferences.AppTheme
import app.instamovies.domain.model.preferences.UserPreferences
import kotlinx.coroutines.flow.Flow

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

interface UserPreferencesRepository {
    val userPreferencesFlow: Flow<UserPreferences>

    suspend fun updateAppTheme(appTheme: AppTheme)
}
