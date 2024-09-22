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

data class TopLevelRoute<T : Any>(
    @StringRes val labelId: Int,
    val route: T,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

val topLevelRoutes =
    listOf(
        TopLevelRoute(
            labelId = Strings.label_home,
            route = Screen.Home,
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
        ),
        TopLevelRoute(
            labelId = Strings.label_movies,
            route = Screen.Movies,
            selectedIcon = Icons.Filled.Movie,
            unselectedIcon = Icons.Outlined.Movie,
        ),
        TopLevelRoute(
            labelId = Strings.label_tv_shows,
            route = Screen.TvShows,
            selectedIcon = Icons.Filled.Tv,
            unselectedIcon = Icons.Outlined.Tv,
        ),
        TopLevelRoute(
            labelId = Strings.label_people,
            route = Screen.Person,
            selectedIcon = Icons.Filled.People,
            unselectedIcon = Icons.Outlined.People,
        ),
    )
