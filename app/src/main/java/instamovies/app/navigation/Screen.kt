package instamovies.app.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Home : Screen()

    @Serializable
    data object HomeContainer : Screen()

    @Serializable
    data object Movies : Screen()

    @Serializable
    data class MovieDetails(
        val movieId: Int,
    ) : Screen()

    @Serializable
    data class PersonDetails(
        val personId: Int,
        val personName: String,
    ) : Screen()

    @Serializable
    data object Person : Screen()

    @Serializable
    data class Search(
        val searchQuery: String,
    ) : Screen()

    @Serializable
    data class TvShowDetails(
        val seriesId: Int,
    ) : Screen()

    @Serializable
    data object TvShows : Screen()
}
