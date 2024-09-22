package instamovies.app.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import instamovies.app.core.util.InstaMoviesNavigationContentPosition
import instamovies.app.presentation.theme.InstaMoviesTheme

private val ContainerWidth = 80.0.dp
private val NavigationRailHeaderPadding: Dp = 8.dp
private val NavigationRailVerticalPadding: Dp = 4.dp

@Composable
fun HomeNavigationRail(
    navController: NavController,
    navigationContentPosition: InstaMoviesNavigationContentPosition,
    onMenuIconClick: () -> Unit,
) {
    NavigationRail {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        // TODO remove custom nav rail positioning when NavRail component supports it. ticket : b/232495216
        Layout(
            content = {
                Column(
                    modifier = Modifier.layoutId(LayoutType.HEADER),
                    verticalArrangement = Arrangement.spacedBy(NavigationRailVerticalPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    NavigationRailItem(
                        selected = false,
                        onClick = onMenuIconClick,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = null,
                            )
                        },
                    )
                    Spacer(modifier = Modifier.height(NavigationRailHeaderPadding))
                }
                Column(
                    modifier = Modifier.layoutId(LayoutType.CONTENT),
                    verticalArrangement = Arrangement.spacedBy(NavigationRailVerticalPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    topLevelRoutes.forEach { topLevelRoute ->
                        val selected =
                            currentDestination?.hierarchy?.any { it.hasRoute(topLevelRoute.route::class) } == true

                        NavigationRailItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(topLevelRoute.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (selected) topLevelRoute.selectedIcon else topLevelRoute.unselectedIcon,
                                    contentDescription = stringResource(id = topLevelRoute.labelId),
                                )
                            },
                            label = {
                                Text(stringResource(id = topLevelRoute.labelId))
                            },
                        )
                    }
                }
            },
            modifier = Modifier.widthIn(max = ContainerWidth),
            measurePolicy = navigationMeasurePolicy(navigationContentPosition),
        )
    }
}

@Preview
@Composable
private fun HomeNavigationRailPreview() {
    InstaMoviesTheme {
        HomeNavigationRail(
            navController = rememberNavController(),
            navigationContentPosition = InstaMoviesNavigationContentPosition.TOP,
            onMenuIconClick = {},
        )
    }
}
