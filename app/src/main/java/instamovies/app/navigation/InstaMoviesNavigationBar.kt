package instamovies.app.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import instamovies.app.presentation.theme.InstaMoviesTheme

@Composable
fun HomeNavigationBar(navController: NavController) {
    val destinations =
        listOf(
            InstaMoviesNavigationScreen.InstaMovies,
            InstaMoviesNavigationScreen.Movies,
            InstaMoviesNavigationScreen.TvShows,
            InstaMoviesNavigationScreen.People,
        )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        destinations.forEach { destination ->
            val selected =
                currentDestination?.hierarchy?.any { it.route == destination.route } == true
            val labelId = destination.label

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    val imageVector =
                        if (selected) {
                            destination.selectedIcon
                        } else {
                            destination.unselectedIcon
                        }
                    Icon(
                        imageVector = imageVector,
                        contentDescription = stringResource(id = labelId),
                    )
                },
                label = { Text(stringResource(id = labelId)) },
            )
        }
    }
}

@Preview
@Composable
private fun HomeNavigationBarPreview() {
    InstaMoviesTheme {
        HomeNavigationBar(navController = rememberNavController())
    }
}
