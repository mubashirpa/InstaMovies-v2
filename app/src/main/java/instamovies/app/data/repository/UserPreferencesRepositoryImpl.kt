package instamovies.app.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import instamovies.app.core.Constants
import instamovies.app.domain.model.preferences.AppTheme
import instamovies.app.domain.model.preferences.UserPreferences
import instamovies.app.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.io.IOException
import javax.inject.Inject

class UserPreferencesRepositoryImpl
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) : UserPreferencesRepository {
        override val userPreferencesFlow: Flow<UserPreferences>
            get() =
                dataStore.data
                    .catch { exception ->
                        if (exception is IOException) {
                            emit(emptyPreferences())
                        } else {
                            throw exception
                        }
                    }.map { preferences ->
                        val appTheme =
                            preferences[Constants.PreferencesKeys.APP_THEME]
                                ?: AppTheme.SYSTEM_DEFAULT.name

                        UserPreferences(appTheme = enumValueOf(appTheme))
                    }

        override suspend fun updateAppTheme(appTheme: AppTheme) {
            dataStore.edit { settings ->
                settings[Constants.PreferencesKeys.APP_THEME] = appTheme.name
            }
        }
    }
