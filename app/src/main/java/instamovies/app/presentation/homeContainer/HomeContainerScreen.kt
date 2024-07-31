package instamovies.app.presentation.homeContainer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import instamovies.app.core.util.InstaMoviesNavigationType
import instamovies.app.core.util.InstaMoviesWindowWidthType
import instamovies.app.domain.model.MediaType
import instamovies.app.navigation.HomeNavHost
import instamovies.app.navigation.InstaMoviesNavigationWrapper
import instamovies.app.navigation.Screen
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
    navigateToMovieDetails: (id: Int) -> Unit,
    navigateToPersonDetails: (id: Int, name: String) -> Unit,
    navigateToSearch: (query: String) -> Unit,
    navigateToTvShowDetails: (id: Int) -> Unit,
) {
    HomeContainerNavigationWrapper(
        uiState = uiState,
        onEvent = onEvent,
        windowWidthType = windowWidthType,
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
    windowWidthType: InstaMoviesWindowWidthType,
    navigateToMovieDetails: (id: Int) -> Unit,
    navigateToPersonDetails: (id: Int, name: String) -> Unit,
    navigateToSearch: (query: String) -> Unit,
    navigateToTvShowDetails: (id: Int) -> Unit,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination =
        navBackStackEntry?.destination?.route ?: Screen.Home.toString()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    InstaMoviesNavigationWrapper(
        navController = navController,
        drawerState = drawerState,
        selectedDestination = selectedDestination,
        navigateToTopLevelDestination = { destination ->
            navController.navigate(destination.screen) {
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
            modifier = Modifier.fillMaxSize(),
            onOpenNavigationDrawer = {
                coroutineScope.launch {
                    drawerState.open()
                }
            },
            navigateToMovieDetails = navigateToMovieDetails,
            navigateToPersonDetails = navigateToPersonDetails,
            navigateToSearch = navigateToSearch,
            navigateToTvShowDetails = navigateToTvShowDetails,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HomeContainerContent(
    uiState: HomeContainerUiState,
    onEvent: (HomeContainerUiEvent) -> Unit,
    navController: NavHostController,
    navigationType: InstaMoviesNavigationType,
    windowWidthType: InstaMoviesWindowWidthType,
    modifier: Modifier = Modifier,
    onOpenNavigationDrawer: () -> Unit,
    navigateToMovieDetails: (id: Int) -> Unit,
    navigateToPersonDetails: (id: Int, name: String) -> Unit,
    navigateToSearch: (query: String) -> Unit,
    navigateToTvShowDetails: (id: Int) -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val trendingList = uiState.trendingResource.data.orEmpty()
    val searchList = uiState.searchResource.data.orEmpty()
    val isDockedSearchBar = navigationType != InstaMoviesNavigationType.BOTTOM_NAVIGATION

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .semantics { isTraversalGroup = true },
        ) {
            HomeAppBar(
                uiState = uiState,
                onEvent = onEvent,
                modifier =
                    Modifier
                        .then(
                            if (isDockedSearchBar) {
                                Modifier
                                    .imePadding()
                                    .padding(top = innerPadding.calculateTopPadding())
                            } else {
                                Modifier.imePadding()
                            },
                        ),
                isDocked = isDockedSearchBar,
                onOpenNavigationDrawer = onOpenNavigationDrawer,
                navigateToProfile = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(context.getString(Strings.coming_soon))
                    }
                },
                navigateToSearch = navigateToSearch,
                content = {
                    LazyColumn(
                        modifier =
                            Modifier.then(
                                if (isDockedSearchBar) {
                                    Modifier
                                } else {
                                    Modifier
//                                        .imePadding()
                                        .imeNestedScroll()
                                },
                            ),
                        content = {
                            if (uiState.searchQuery.isNotEmpty()) {
                                items(items = searchList, key = { it.id!! }) { result ->
                                    SearchListItem(result = result) { mediaType, id, name ->
                                        onEvent(HomeContainerUiEvent.OnSearch)
                                        when (mediaType) {
                                            MediaType.MOVIE -> navigateToMovieDetails(id)
                                            MediaType.PERSON -> navigateToPersonDetails(id, name)
                                            MediaType.TV -> navigateToTvShowDetails(id)
                                        }
                                    }
                                }
                            } else {
                                items(items = trendingList, key = { it.id!! }) { result ->
                                    SearchListItem(result = result) { mediaType, id, name ->
                                        onEvent(HomeContainerUiEvent.OnSearch)
                                        when (mediaType) {
                                            MediaType.MOVIE -> navigateToMovieDetails(id)
                                            MediaType.PERSON -> navigateToPersonDetails(id, name)
                                            MediaType.TV -> navigateToTvShowDetails(id)
                                        }
                                    }
                                }
                            }
                        },
                    )
                },
            )
            HomeNavHost(
                navController = navController,
                uiState = uiState,
                onEvent = onEvent,
                navigationType = navigationType,
                windowWidthType = windowWidthType,
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(
                            top =
                                innerPadding
                                    .calculateTopPadding()
                                    .plus(72.dp),
                        ),
                navigateToMovieDetails = navigateToMovieDetails,
                navigateToPersonDetails = navigateToPersonDetails,
                navigateToTvShowDetails = navigateToTvShowDetails,
            )
        }
    }
}
