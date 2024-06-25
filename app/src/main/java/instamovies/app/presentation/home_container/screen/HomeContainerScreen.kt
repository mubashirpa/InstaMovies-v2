package instamovies.app.presentation.home_container.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
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
    HomeContainerNavigationWrapper(
        uiState = uiState,
        onEvent = onEvent,
        widthSizeClass = widthSizeClass,
        navigationType = navigationType,
        navigationContentPosition = navigationContentPosition,
        navigateToMovieDetails = navigateToMovieDetails,
        navigateToPersonDetails = navigateToPersonDetails,
        navigateToSearch = navigateToSearch,
        navigateToTvShowDetails = navigateToTvShowDetails,
    )
}

@Composable
private fun HomeContainerNavigationWrapper(
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
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    BackHandler(enabled = drawerState.isOpen) {
        coroutineScope.launch { drawerState.close() }
    }

    val content: @Composable () -> Unit = {
        HomeContainerContent(
            uiState = uiState,
            onEvent = onEvent,
            widthSizeClass = widthSizeClass,
            navigationType = navigationType,
            navigationContentPosition = navigationContentPosition,
            navController = navController,
            drawerState = drawerState,
            modifier = Modifier.fillMaxSize(),
            navigateToMovieDetails = navigateToMovieDetails,
            navigateToPersonDetails = navigateToPersonDetails,
            navigateToSearch = navigateToSearch,
            navigateToTvShowDetails = navigateToTvShowDetails,
        )
    }

    if (navigationType == InstaMoviesNavigationType.PERMANENT_NAVIGATION_DRAWER) {
        InstaMoviesPermanentNavigationDrawer(
            navController = navController,
            navigationContentPosition = navigationContentPosition,
            content = content,
        )
    } else {
        InstaMoviesModalNavigationDrawer(
            navController = navController,
            drawerState = drawerState,
            navigationContentPosition = navigationContentPosition,
            navigationType = navigationType,
            content = content,
        )
    }
}

@Composable
private fun HomeContainerContent(
    uiState: HomeContainerUiState,
    onEvent: (HomeContainerUiEvent) -> Unit,
    widthSizeClass: WindowWidthSizeClass,
    navigationType: InstaMoviesNavigationType,
    navigationContentPosition: InstaMoviesNavigationContentPosition,
    navController: NavHostController,
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
    navigateToMovieDetails: (id: Int) -> Unit,
    navigateToPersonDetails: (id: Int, name: String) -> Unit,
    navigateToSearch: (query: String) -> Unit,
    navigateToTvShowDetails: (id: Int) -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val trendingList = uiState.trendingResource.data.orEmpty()
    val bottomNavigationVisible = navigationType == InstaMoviesNavigationType.BOTTOM_NAVIGATION

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            val isNavigationBarVisible = bottomNavigationVisible && !uiState.searchbarExpanded

            SnackbarHost(
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
        },
    ) { innerPadding ->
        Row(modifier = Modifier.fillMaxSize()) {
            AnimatedVisibility(visible = navigationType == InstaMoviesNavigationType.NAVIGATION_RAIL) {
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
                        coroutineScope.launch {
                            drawerState.open()
                        }
                    },
                    navigateToMovieDetails = navigateToMovieDetails,
                    navigateToPersonDetails = navigateToPersonDetails,
                    navigateToProfile = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(context.getString(Strings.coming_soon))
                        }
                    },
                    navigateToSearch = navigateToSearch,
                    navigateToTvShowDetails = navigateToTvShowDetails,
                )
                Column(modifier = Modifier.fillMaxSize()) {
                    HomeNavHost(
                        navController = navController,
                        uiState = uiState,
                        onEvent = onEvent,
                        widthSizeClass = widthSizeClass,
                        navigationType = navigationType,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(top = innerPadding.calculateTopPadding().plus(72.dp)),
                        navigateToMovieDetails = navigateToMovieDetails,
                        navigateToPersonDetails = navigateToPersonDetails,
                        navigateToTvShowDetails = navigateToTvShowDetails,
                    )
                    AnimatedVisibility(visible = bottomNavigationVisible) {
                        HomeNavigationBar(navController = navController)
                    }
                }
            }
        }
    }
}
