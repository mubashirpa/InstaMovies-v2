package instamovies.app.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldLayout
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import instamovies.app.core.util.InstaMoviesNavigationContentPosition
import kotlinx.coroutines.launch

private fun WindowSizeClass.isCompact() =
    windowWidthSizeClass == WindowWidthSizeClass.COMPACT ||
        windowHeightSizeClass == WindowHeightSizeClass.COMPACT

class InstaMoviesNavigationSuiteScope(
    val navigationSuiteType: NavigationSuiteType,
)

@Composable
fun InstaMoviesNavigationWrapper(
    navController: NavHostController,
    drawerState: DrawerState,
    navigateToTopLevelDestination: (TopLevelRoute<out Screen>) -> Unit,
    content: @Composable InstaMoviesNavigationSuiteScope.() -> Unit,
) {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val windowSize =
        with(LocalDensity.current) {
            currentWindowSize().toSize().toDpSize()
        }

    val navigationLayoutType =
        when {
            adaptiveInfo.windowPosture.isTabletop || adaptiveInfo.windowSizeClass.isCompact() -> {
                NavigationSuiteType.NavigationBar
            }

            adaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED &&
                windowSize.width >= 1200.dp -> {
                NavigationSuiteType.NavigationDrawer
            }

            else -> {
                NavigationSuiteType.NavigationRail
            }
        }

    val navigationContentPosition =
        when (adaptiveInfo.windowSizeClass.windowHeightSizeClass) {
            WindowHeightSizeClass.MEDIUM, WindowHeightSizeClass.EXPANDED -> {
                InstaMoviesNavigationContentPosition.CENTER
            }

            else -> {
                InstaMoviesNavigationContentPosition.TOP
            }
        }

    val coroutineScope = rememberCoroutineScope()
    // Avoid opening the modal drawer when there is a permanent drawer or a bottom nav bar,
    // but always allow closing an open drawer.
    val gesturesEnabled =
        drawerState.isOpen || navigationLayoutType == NavigationSuiteType.NavigationRail

    BackHandler(enabled = drawerState.isOpen) {
        coroutineScope.launch {
            drawerState.close()
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    ModalNavigationDrawer(
        drawerContent = {
            ModalNavigationDrawerContent(
                currentDestination = currentDestination,
                navigationContentPosition = navigationContentPosition,
                isNavigationRail = navigationLayoutType == NavigationSuiteType.NavigationRail,
                navigateToTopLevelDestination = navigateToTopLevelDestination,
                onDrawerClicked = {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                },
            )
        },
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        content = {
            NavigationSuiteScaffoldLayout(
                navigationSuite = {
                    when (navigationLayoutType) {
                        NavigationSuiteType.NavigationBar -> {
                            HomeNavigationBar(navController = navController)
                        }

                        NavigationSuiteType.NavigationRail -> {
                            HomeNavigationRail(
                                navController = navController,
                                navigationContentPosition = navigationContentPosition,
                                onMenuIconClick = {
                                    coroutineScope.launch {
                                        drawerState.open()
                                    }
                                },
                            )
                        }

                        NavigationSuiteType.NavigationDrawer -> {
                            PermanentNavigationDrawerContent(
                                currentDestination = currentDestination,
                                navigationContentPosition = navigationContentPosition,
                                navigateToTopLevelDestination = navigateToTopLevelDestination,
                            )
                        }
                    }
                },
                layoutType = navigationLayoutType,
            ) {
                InstaMoviesNavigationSuiteScope(navigationLayoutType).content()
            }
        },
    )
}
