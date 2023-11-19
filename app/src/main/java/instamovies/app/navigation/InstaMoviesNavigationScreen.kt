package instamovies.app.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.ui.graphics.vector.ImageVector
import instamovies.app.R.string as Strings

sealed class InstaMoviesNavigationScreen(
    var route: String,
    var selectedIcon: ImageVector,
    var unselectedIcon: ImageVector,
    @StringRes val label: Int,
) {
    data object InstaMovies : InstaMoviesNavigationScreen(
        Screen.HomeScreen.route,
        Icons.Filled.Home,
        Icons.Outlined.Home,
        Strings.label_home,
    )

    data object Movies : InstaMoviesNavigationScreen(
        Screen.MoviesScreen.route,
        Icons.Filled.Movie,
        Icons.Outlined.Movie,
        Strings.label_movies,
    )

    data object TvShows : InstaMoviesNavigationScreen(
        Screen.TvShowsScreen.route,
        Icons.Filled.Tv,
        Icons.Outlined.Tv,
        Strings.label_tv_shows,
    )

    data object People : InstaMoviesNavigationScreen(
        Screen.PersonScreen.route,
        Icons.Filled.People,
        Icons.Outlined.People,
        Strings.label_people,
    )
}
