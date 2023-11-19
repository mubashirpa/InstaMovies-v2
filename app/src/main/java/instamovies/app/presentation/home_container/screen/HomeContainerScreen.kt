package instamovies.app.presentation.home_container.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import instamovies.app.core.util.InstaMoviesNavigationContentPosition
import instamovies.app.core.util.InstaMoviesNavigationType
import instamovies.app.navigation.HomeNavHost
import instamovies.app.navigation.HomeNavigationBar
import instamovies.app.navigation.HomeNavigationRail
import instamovies.app.navigation.InstaMoviesModalNavigationDrawer
import instamovies.app.navigation.InstaMoviesPermanentNavigationDrawer
import instamovies.app.presentation.components.InstaMoviesSnackbarHost
import instamovies.app.presentation.home_container.HomeContainerUiEvent
import instamovies.app.presentation.home_container.HomeContainerUiState
import instamovies.app.presentation.home_container.screen.components.HomeAppBar
import kotlinx.coroutines.launch
import instamovies.app.R.string as Strings

@Composable
fun HomeContainerScreen(
    uiState: HomeContainerUiState,
    onEvent: (HomeContainerUiEvent) -> Unit,
    widthSizeClass: WindowWidthSizeClass,
    navigationType: InstaMoviesNavigationType,
    navigationContentPosition: InstaMoviesNavigationContentPosition,
    navigateToMovieDetails: (id: Int) -> Unit,
    navigateToPersonDetails: (id: Int, name: String) -> Unit,
    navigateToSearch: (query: String) -> Unit,
    navigateToTvShowDetails: (id: Int) -> Unit,
) {
    val homeNavController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val navigationRailVisible = navigationType == InstaMoviesNavigationType.NAVIGATION_RAIL
    val bottomNavigationVisible = navigationType == InstaMoviesNavigationType.BOTTOM_NAVIGATION
    val trendingList = uiState.trendingResource.data.orEmpty()

    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }

    val content: @Composable () -> Unit = {
        Scaffold(
            modifier =
                Modifier
                    .fillMaxSize()
                    .imePadding(),
            snackbarHost = {
                val isNavigationBarVisible = bottomNavigationVisible && !uiState.searchbarActive
                Box(modifier = Modifier.fillMaxWidth()) {
                    InstaMoviesSnackbarHost(
                        hostState = snackbarHostState,
                        modifier =
                            if (isNavigationBarVisible) {
                                Modifier.padding(
                                    bottom = 80.dp,
                                ) // NavigationBarHeight, to show Snackbar above NavigationBar if NavigationBar is shown
                            } else {
                                Modifier
                            },
                    )
                }
            },
        ) { innerPadding ->
            Row(modifier = Modifier.fillMaxSize()) {
                AnimatedVisibility(visible = navigationRailVisible) {
                    HomeNavigationRail(
                        navController = homeNavController,
                        navigationContentPosition = navigationContentPosition,
                        onMenuIconClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        },
                    )
                }
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .semantics { isTraversalGroup = true },
                ) {
                    HomeAppBar(
                        uiState = uiState,
                        onEvent = onEvent,
                        navigationType = navigationType,
                        trendingList = trendingList,
                        onMenuIconClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        },
                        navigateToMovieDetails = navigateToMovieDetails,
                        navigateToPersonDetails = navigateToPersonDetails,
                        navigateToProfile = {
                            scope.launch {
                                snackbarHostState.showSnackbar(context.getString(Strings.coming_soon))
                            }
                        },
                        navigateToSearch = navigateToSearch,
                        navigateToTvShowDetails = navigateToTvShowDetails,
                    )
                    Column(modifier = Modifier.fillMaxSize()) {
                        HomeContainerScreenContent(
                            uiState = uiState,
                            onEvent = onEvent,
                            homeNavController = homeNavController,
                            widthSizeClass = widthSizeClass,
                            navigationType = navigationType,
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .weight(1F)
                                    .padding(top = innerPadding.calculateTopPadding() + 72.dp /*HomeAppBarHeight*/)
                                    .then(
                                        if (!bottomNavigationVisible) {
                                            Modifier.padding(
                                                bottom = innerPadding.calculateBottomPadding(),
                                            )
                                        } else {
                                            Modifier
                                        },
                                    ),
                            navigateToMovieDetails = navigateToMovieDetails,
                            navigateToPersonDetails = navigateToPersonDetails,
                            navigateToTvShowDetails = navigateToTvShowDetails,
                        )
                        AnimatedVisibility(visible = bottomNavigationVisible) {
                            HomeNavigationBar(navController = homeNavController)
                        }
                    }
                }
            }
        }
    }

    if (navigationType == InstaMoviesNavigationType.PERMANENT_NAVIGATION_DRAWER) {
        InstaMoviesPermanentNavigationDrawer(
            navController = homeNavController,
            navigationContentPosition = navigationContentPosition,
            content = content,
        )
    } else {
        InstaMoviesModalNavigationDrawer(
            navController = homeNavController,
            drawerState = drawerState,
            navigationContentPosition = navigationContentPosition,
            navigationType = navigationType,
            content = content,
        )
    }
}

@Composable
private fun HomeContainerScreenContent(
    uiState: HomeContainerUiState,
    onEvent: (HomeContainerUiEvent) -> Unit,
    homeNavController: NavHostController,
    widthSizeClass: WindowWidthSizeClass,
    navigationType: InstaMoviesNavigationType,
    modifier: Modifier = Modifier,
    navigateToMovieDetails: (id: Int) -> Unit,
    navigateToPersonDetails: (id: Int, name: String) -> Unit,
    navigateToTvShowDetails: (id: Int) -> Unit,
) {
    HomeNavHost(
        navController = homeNavController,
        uiState = uiState,
        onEvent = onEvent,
        widthSizeClass = widthSizeClass,
        navigationType = navigationType,
        modifier = modifier,
        navigateToMovieDetails = navigateToMovieDetails,
        navigateToPersonDetails = navigateToPersonDetails,
        navigateToTvShowDetails = navigateToTvShowDetails,
    )
}
