package app.instamovies.core

import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {
    const val TMDB_BASE_URL = "https://api.themoviedb.org/"
    private const val TMDB_IMAGES_BASE_URL = "https://image.tmdb.org/t/p/"
    const val TMDB_BACKDROP_PREFIX = "${TMDB_IMAGES_BASE_URL}w780"
    const val TMDB_POSTER_PREFIX = "${TMDB_IMAGES_BASE_URL}w500"
    const val TMDB_PROFILE_PREFIX = "${TMDB_IMAGES_BASE_URL}w185"
    const val TMDB_STILL_PREFIX = "${TMDB_IMAGES_BASE_URL}w185"
    const val SUPPORT_EMAIL = "mubashirpa2002@gmail.com"

    object PreferencesKeys {
        val APP_THEME = stringPreferencesKey("app_theme")
    }
}
