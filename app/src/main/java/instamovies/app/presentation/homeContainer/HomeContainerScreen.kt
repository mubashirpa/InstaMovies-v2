package instamovies.app.presentation.homeContainer

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import instamovies.app.core.util.InstaMoviesNavigationType
import instamovies.app.core.util.InstaMoviesWindowWidthType
import instamovies.app.domain.model.MediaType
import instamovies.app.navigation.HomeNavHost
import instamovies.app.navigation.InstaMoviesNavigationWrapper
import instamovies.app.presentation.homeContainer.components.HomeAppBar
import instamovies.app.presentation.homeContainer.components.SearchListItem
import kotlinx.coroutines.launch
import instamovies.app.R.string as Strings

private fun NavigationSuiteType.toInstaMoviesNavigationType() =
    when (this) {
        NavigationSuiteType.NavigationBar -> InstaMoviesNavigationType.BOTTOM_NAVIGATION
        NavigationSuiteType.NavigationRail -> InstaMoviesNavigationType.NAVIGATION_RAIL
        NavigationSuiteType.NavigationDrawer -> InstaMoviesNavigationType.PERMANENT_NAVIGATION_DRAWER
        else -> InstaMoviesNavigationType.BOTTOM_NAVIGATION
    }

@Composable
fun HomeContainerScreen(
    uiState: HomeContainerUiState,
    onEvent: (HomeContainerUiEvent) -> Unit,
    windowWidthType: InstaMoviesWindowWidthType,
    onNavigateToMovieDetails: (id: Int) -> Unit,
    onNavigateToPersonDetails: (id: Int, name: String) -> Unit,
    onNavigateToSearch: (query: String) -> Unit,
    onNavigateToTvShowDetails: (id: Int) -> Unit,
) {
    HomeContainerNavigationWrapper(
        uiState = uiState,
        onEvent = onEvent,
        windowWidthType = windowWidthType,
        onNavigateToMovieDetails = onNavigateToMovieDetails,
        onNavigateToPersonDetails = onNavigateToPersonDetails,
        onNavigateToSearch = onNavigateToSearch,
        onNavigateToTvShowDetails = onNavigateToTvShowDetails,
    )
}

@Composable
private fun HomeContainerNavigationWrapper(
    uiState: HomeContainerUiState,
    onEvent: (HomeContainerUiEvent) -> Unit,
    windowWidthType: InstaMoviesWindowWidthType,
    onNavigateToMovieDetails: (id: Int) -> Unit,
    onNavigateToPersonDetails: (id: Int, name: String) -> Unit,
    onNavigateToSearch: (query: String) -> Unit,
    onNavigateToTvShowDetails: (id: Int) -> Unit,
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    InstaMoviesNavigationWrapper(
        navController = navController,
        drawerState = drawerState,
        navigateToTopLevelDestination = { topLevelRoute ->
            navController.navigate(topLevelRoute.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        },
    ) {
        HomeContainerContent(
            uiState = uiState,
            onEvent = onEvent,
            navController = navController,
            navigationType = navigationSuiteType.toInstaMoviesNavigationType(),
            windowWidthType = windowWidthType,
            onNavigationIconClick = {
                coroutineScope.launch {
                    drawerState.open()
                }
            },
            onNavigateToMovieDetails = onNavigateToMovieDetails,
            onNavigateToPersonDetails = onNavigateToPersonDetails,
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToTvShowDetails = onNavigateToTvShowDetails,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun HomeContainerContent(
    uiState: HomeContainerUiState,
    onEvent: (HomeContainerUiEvent) -> Unit,
    navController: NavHostController,
    navigationType: InstaMoviesNavigationType,
    windowWidthType: InstaMoviesWindowWidthType,
    onNavigationIconClick: () -> Unit,
    onNavigateToMovieDetails: (id: Int) -> Unit,
    onNavigateToPersonDetails: (id: Int, name: String) -> Unit,
    onNavigateToSearch: (query: String) -> Unit,
    onNavigateToTvShowDetails: (id: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val trendingList = uiState.trendingResource.data.orEmpty()
    val searchList = uiState.searchResource.data.orEmpty()
    val isBottomNavigationVisible = navigationType == InstaMoviesNavigationType.BOTTOM_NAVIGATION
    val scrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()
    val searchBarState = rememberSearchBarState()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            HomeAppBar(
                uiState = uiState,
                searchBarState = searchBarState,
                isDocked = !isBottomNavigationVisible,
                scrollBehavior = scrollBehavior,
                onNavigationIconClick = onNavigationIconClick,
                onNavigateToProfile = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(context.getString(Strings.coming_soon))
                    }
                },
                onNavigateToSearch = onNavigateToSearch,
                content = {
                    LazyColumn(
                        modifier =
                            if (!isBottomNavigationVisible) {
                                Modifier
                            } else {
                                Modifier.imeNestedScroll()
                            },
                        content = {
                            when {
                                uiState.searchQueryState.text.isNotEmpty() -> {
                                    items(items = searchList, key = { it.id!! }) { result ->
                                        SearchListItem(result = result) { mediaType, id, name ->
                                            coroutineScope
                                                .launch {
                                                    searchBarState.animateToCollapsed()
                                                }.invokeOnCompletion {
                                                    when (mediaType) {
                                                        MediaType.MOVIE -> {
                                                            onNavigateToMovieDetails(id)
                                                        }

                                                        MediaType.PERSON -> {
                                                            onNavigateToPersonDetails(id, name)
                                                        }

                                                        MediaType.TV -> {
                                                            onNavigateToTvShowDetails(id)
                                                        }
                                                    }
                                                }
                                        }
                                    }
                                }

                                else -> {
                                    items(items = trendingList, key = { it.id!! }) { result ->
                                        SearchListItem(result = result) { mediaType, id, name ->
                                            coroutineScope
                                                .launch {
                                                    searchBarState.animateToCollapsed()
                                                }.invokeOnCompletion {
                                                    when (mediaType) {
                                                        MediaType.MOVIE -> {
                                                            onNavigateToMovieDetails(id)
                                                        }

                                                        MediaType.PERSON -> {
                                                            onNavigateToPersonDetails(id, name)
                                                        }

                                                        MediaType.TV -> {
                                                            onNavigateToTvShowDetails(id)
                                                        }
                                                    }
                                                }
                                        }
                                    }
                                }
                            }
                        },
                    )
                },
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier =
                    if (isBottomNavigationVisible) {
                        Modifier
                    } else {
                        Modifier.navigationBarsPadding()
                    },
            )
        },
        contentWindowInsets =
            WindowInsets.systemBars
                .union(WindowInsets.displayCutout)
                .only(WindowInsetsSides.Top),
    ) { innerPadding ->
        HomeNavHost(
            navController = navController,
            uiState = uiState,
            onEvent = onEvent,
            navigationType = navigationType,
            windowWidthType = windowWidthType,
            navigateToMovieDetails = onNavigateToMovieDetails,
            navigateToPersonDetails = onNavigateToPersonDetails,
            navigateToTvShowDetails = onNavigateToTvShowDetails,
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding),
        )
    }
}
