package instamovies.app.navigation

import kotlinx.serialization.Serializable

sealed class Route {
    @Serializable
    data object Home : Route()

    @Serializable
    data object HomeContainer : Route()

    @Serializable
    data object Movies : Route()

    @Serializable
    data class MovieDetails(
        val movieId: Int,
    ) : Route()

    @Serializable
    data class PersonDetails(
        val personId: Int,
        val personName: String,
    ) : Route()

    @Serializable
    data object Person : Route()

    @Serializable
    data class Search(
        val searchQuery: String,
    ) : Route()

    @Serializable
    data class TvShowDetails(
        val seriesId: Int,
    ) : Route()

    @Serializable
    data object TvShows : Route()

    @Serializable
    data object Settings : Route()
}
