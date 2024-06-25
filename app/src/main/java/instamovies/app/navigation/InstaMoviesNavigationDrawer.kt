package instamovies.app.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import instamovies.app.core.util.InstaMoviesNavigationContentPosition
import instamovies.app.core.util.InstaMoviesNavigationType
import instamovies.app.presentation.theme.InstaMoviesTheme
import kotlinx.coroutines.launch
import instamovies.app.R.string as Strings

@Composable
fun InstaMoviesModalNavigationDrawer(
    navController: NavController,
    drawerState: DrawerState,
    navigationContentPosition: InstaMoviesNavigationContentPosition,
    navigationType: InstaMoviesNavigationType,
    content: @Composable () -> Unit,
) {
    val destinations =
        listOf(
            InstaMoviesNavigationScreen.InstaMovies,
            InstaMoviesNavigationScreen.Movies,
            InstaMoviesNavigationScreen.TvShows,
            InstaMoviesNavigationScreen.People,
        )
    val items = listOf(DrawerItem.Settings, DrawerItem.Contact)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val scope = rememberCoroutineScope()
    val isNavigationRail = navigationType == InstaMoviesNavigationType.NAVIGATION_RAIL

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                // TODO remove custom nav drawer content positioning when NavDrawer component supports it. ticket : b/232495216
                Layout(
                    content = {
                        Column(
                            modifier = Modifier.layoutId(LayoutType.HEADER),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Row(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .height(72.dp)
                                        .padding(horizontal = 28.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = stringResource(id = Strings.app_name).uppercase(),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                                IconButton(
                                    onClick = {
                                        scope.launch { drawerState.close() }
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.MenuOpen,
                                        contentDescription = null,
                                    )
                                }
                            }
                        }
                        Column(
                            modifier =
                                Modifier
                                    .layoutId(LayoutType.CONTENT)
                                    .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            if (isNavigationRail) {
                                destinations.forEach { destination ->
                                    // TODO("Fix selected destination")
                                    val selected =
                                        currentDestination?.hierarchy?.any {
                                            it.route?.endsWith(
                                                destination.screen.toString(),
                                            ) == true
                                        } == true
                                    val labelId = destination.label

                                    NavigationDrawerItem(
                                        label = {
                                            Text(stringResource(id = labelId))
                                        },
                                        selected = selected,
                                        onClick = {
                                            scope.launch { drawerState.close() }
                                            navController.navigate(destination.screen) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
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
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                DividerItem(modifier = Modifier.padding(horizontal = 28.dp))
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                            items.forEachIndexed { _, drawerItem ->
                                NavigationDrawerItem(
                                    label = {
                                        Text(stringResource(id = drawerItem.labelId))
                                    },
                                    selected = false,
                                    onClick = {
                                        scope.launch { drawerState.close() }
                                    },
                                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                                    icon = {
                                        Icon(
                                            imageVector = drawerItem.icon,
                                            contentDescription = null,
                                        )
                                    },
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    },
                    measurePolicy =
                        navigationMeasurePolicy(
                            if (isNavigationRail) {
                                navigationContentPosition
                            } else {
                                InstaMoviesNavigationContentPosition.TOP
                            },
                        ),
                )
            }
        },
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        content = content,
    )
}

@Composable
fun InstaMoviesPermanentNavigationDrawer(
    navController: NavController,
    navigationContentPosition: InstaMoviesNavigationContentPosition,
    content: @Composable () -> Unit,
) {
    val destinations =
        listOf(
            InstaMoviesNavigationScreen.InstaMovies,
            InstaMoviesNavigationScreen.Movies,
            InstaMoviesNavigationScreen.TvShows,
            InstaMoviesNavigationScreen.People,
        )
    val items = listOf(DrawerItem.Settings, DrawerItem.Contact)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    PermanentNavigationDrawer(
        drawerContent = {
            PermanentDrawerSheet(modifier = Modifier.width(300.dp)) {
                // TODO remove custom nav drawer content positioning when NavDrawer component supports it. ticket : b/232495216
                Layout(
                    content = {
                        Column(
                            modifier = Modifier.layoutId(LayoutType.HEADER),
                            horizontalAlignment = Alignment.Start,
                        ) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = stringResource(id = Strings.app_name).uppercase(),
                                modifier =
                                    Modifier.padding(
                                        horizontal = 28.dp,
                                        vertical = 16.dp,
                                    ),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                        Column(
                            modifier =
                                Modifier
                                    .layoutId(LayoutType.CONTENT)
                                    .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            destinations.forEach { destination ->
                                // TODO("Fix selected destination")
                                val selected =
                                    currentDestination?.hierarchy?.any {
                                        it.route?.endsWith(
                                            destination.screen.toString(),
                                        ) == true
                                    } == true
                                val labelId = destination.label

                                NavigationDrawerItem(
                                    label = {
                                        Text(stringResource(id = labelId))
                                    },
                                    selected = selected,
                                    onClick = {
                                        navController.navigate(destination.screen) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
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
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            DividerItem(modifier = Modifier.padding(horizontal = 28.dp))
                            Spacer(modifier = Modifier.height(12.dp))
                            items.forEachIndexed { _, drawerItem ->
                                NavigationDrawerItem(
                                    label = {
                                        Text(stringResource(id = drawerItem.labelId))
                                    },
                                    selected = false,
                                    onClick = {},
                                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                                    icon = {
                                        Icon(
                                            imageVector = drawerItem.icon,
                                            contentDescription = null,
                                        )
                                    },
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    },
                    measurePolicy = navigationMeasurePolicy(navigationContentPosition),
                )
            }
        },
        content = content,
    )
}

@Composable
private fun DividerItem(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
    )
}

private sealed class DrawerItem(
    @StringRes var labelId: Int,
    var icon: ImageVector,
) {
    data object Settings : DrawerItem(Strings.label_settings, Icons.Outlined.Settings)

    data object Contact : DrawerItem(Strings.label_contact, Icons.Outlined.Mail)
}

@Preview
@Composable
private fun InstaMoviesModalNavigationDrawerPreview() {
    InstaMoviesTheme(darkTheme = true) {
        InstaMoviesModalNavigationDrawer(
            navController = rememberNavController(),
            drawerState = rememberDrawerState(initialValue = DrawerValue.Open),
            navigationContentPosition = InstaMoviesNavigationContentPosition.TOP,
            navigationType = InstaMoviesNavigationType.BOTTOM_NAVIGATION,
            content = {
                Surface(modifier = Modifier.fillMaxSize()) {}
            },
        )
    }
}

@Preview(device = "spec:width=1920dp,height=1080dp,dpi=160")
@Composable
private fun InstaMoviesPermanentNavigationDrawerPreview() {
    InstaMoviesTheme(darkTheme = true) {
        InstaMoviesPermanentNavigationDrawer(
            navController = rememberNavController(),
            navigationContentPosition = InstaMoviesNavigationContentPosition.CENTER,
            content = {
                Surface(modifier = Modifier.fillMaxSize()) {}
            },
        )
    }
}
