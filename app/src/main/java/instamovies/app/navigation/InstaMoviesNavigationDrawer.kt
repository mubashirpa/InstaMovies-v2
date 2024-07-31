package instamovies.app.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import instamovies.app.core.util.InstaMoviesNavigationContentPosition
import instamovies.app.R.string as Strings

@Composable
fun ModalNavigationDrawerContent(
    selectedDestination: String,
    navigationContentPosition: InstaMoviesNavigationContentPosition,
    isNavigationRail: Boolean,
    navigateToTopLevelDestination: (InstaMoviesNavigationScreen) -> Unit,
    onDrawerClicked: () -> Unit = {},
) {
    val destinations =
        listOf(
            InstaMoviesNavigationScreen.InstaMovies,
            InstaMoviesNavigationScreen.Movies,
            InstaMoviesNavigationScreen.TvShows,
            InstaMoviesNavigationScreen.People,
        )
    val items = listOf(DrawerItem.Settings, DrawerItem.Contact)

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
                        IconButton(onClick = onDrawerClicked) {
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
                                selectedDestination.endsWith(destination.screen.toString())
                            val labelId = destination.label

                            NavigationDrawerItem(
                                label = {
                                    Text(stringResource(id = labelId))
                                },
                                selected = selected,
                                onClick = {
                                    navigateToTopLevelDestination(destination)
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
                            onClick = onDrawerClicked, // TODO
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
}

@Composable
fun PermanentNavigationDrawerContent(
    selectedDestination: String,
    navigationContentPosition: InstaMoviesNavigationContentPosition,
    navigateToTopLevelDestination: (InstaMoviesNavigationScreen) -> Unit,
) {
    val destinations =
        listOf(
            InstaMoviesNavigationScreen.InstaMovies,
            InstaMoviesNavigationScreen.Movies,
            InstaMoviesNavigationScreen.TvShows,
            InstaMoviesNavigationScreen.People,
        )
    val items = listOf(DrawerItem.Settings, DrawerItem.Contact)

    PermanentDrawerSheet(modifier = Modifier.width(240.dp)) {
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
                        val selected = selectedDestination.endsWith(destination.screen.toString())
                        val labelId = destination.label

                        NavigationDrawerItem(
                            label = {
                                Text(stringResource(id = labelId))
                            },
                            selected = selected,
                            onClick = {
                                navigateToTopLevelDestination(destination)
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
