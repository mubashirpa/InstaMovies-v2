package instamovies.app.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.window.layout.DisplayFeature
import instamovies.app.core.util.InstaMoviesContentType
import instamovies.app.core.util.InstaMoviesNavigationContentPosition
import instamovies.app.core.util.InstaMoviesNavigationType
import instamovies.app.presentation.home_container.HomeContainerViewModel
import instamovies.app.presentation.home_container.screen.HomeContainerScreen
import instamovies.app.presentation.movie_details.MovieDetailsViewModel
import instamovies.app.presentation.movie_details.screen.MovieDetailsScreen
import instamovies.app.presentation.person_details.PersonDetailsViewModel
import instamovies.app.presentation.person_details.screen.PersonDetailsScreen
import instamovies.app.presentation.search.SearchViewModel
import instamovies.app.presentation.search.screen.SearchScreen
import instamovies.app.presentation.tv_show_details.TvShowDetailsViewModel
import instamovies.app.presentation.tv_show_details.screen.TvShowDetailsScreen

@Composable
fun InstaMoviesNavHost(
    navController: NavHostController,
    widthSizeClass: WindowWidthSizeClass,
    navigationType: InstaMoviesNavigationType,
    contentType: InstaMoviesContentType,
    displayFeatures: List<DisplayFeature>,
    navigationContentPosition: InstaMoviesNavigationContentPosition,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeContainerScreen.route,
        modifier = modifier,
    ) {
        composable(route = Screen.HomeContainerScreen.route) {
            val viewModel: HomeContainerViewModel = hiltViewModel()
            HomeContainerScreen(
                uiState = viewModel.uiState,
                onEvent = viewModel::onEvent,
                widthSizeClass = widthSizeClass,
                navigationType = navigationType,
                navigationContentPosition = navigationContentPosition,
                navigateToMovieDetails = {
                    navController.navigate(Screen.MovieDetailsScreen.withArgs(it))
                },
                navigateToPersonDetails = { id, name ->
                    navController.navigate(Screen.PersonDetailsScreen.withArgs(id, name))
                },
                navigateToSearch = {
                    navController.navigate(Screen.SearchScreen.withArgs(it))
                },
                navigateToTvShowDetails = {
                    navController.navigate(Screen.TvShowDetailsScreen.withArgs(it))
                },
            )
        }
        composable(
            route = "${Screen.MovieDetailsScreen.route}${Screen.MovieDetailsScreen.args}",
            arguments =
                listOf(
                    navArgument(Routes.Args.MOVIE_DETAILS_MOVIE_ID) {
                        type = NavType.IntType
                        defaultValue = 0
                    },
                ),
        ) {
            val viewModel: MovieDetailsViewModel = hiltViewModel()
            MovieDetailsScreen(
                contentType = contentType,
                displayFeatures = displayFeatures,
                uiState = viewModel.uiState,
                onEvent = viewModel::onEvent,
                navigateToMovieDetails = {
                    navController.navigate(Screen.MovieDetailsScreen.withArgs(it))
                },
                navigateToPersonDetails = { id, name ->
                    navController.navigate(Screen.PersonDetailsScreen.withArgs(id, name))
                },
                onBackPressed = {
                    navController.navigateUp()
                },
            )
        }
        composable(
            route = "${Screen.PersonDetailsScreen.route}${Screen.PersonDetailsScreen.args}",
            arguments =
                listOf(
                    navArgument(Routes.Args.PERSON_DETAILS_PERSON_ID) {
                        type = NavType.IntType
                        defaultValue = 0
                    },
                ),
        ) { backStackEntry ->
            val viewModel: PersonDetailsViewModel = hiltViewModel()
            val title =
                backStackEntry.arguments?.getString(Routes.Args.PERSON_DETAILS_TITLE).orEmpty()
            PersonDetailsScreen(
                contentType = contentType,
                displayFeatures = displayFeatures,
                uiState = viewModel.uiState,
                onEvent = viewModel::onEvent,
                title = title,
                navigateToMovieDetails = {
                    navController.navigate(Screen.MovieDetailsScreen.withArgs(it))
                },
                navigateToTvShowDetails = {
                    navController.navigate(Screen.TvShowDetailsScreen.withArgs(it))
                },
                onBackPressed = {
                    navController.navigateUp()
                },
            )
        }
        composable(
            route = "${Screen.SearchScreen.route}${Screen.SearchScreen.args}",
            arguments =
                listOf(
                    navArgument(Routes.Args.SEARCH_SCREEN_QUERY) {
                        type = NavType.StringType
                    },
                ),
        ) { backStackEntry ->
            val viewModel: SearchViewModel = hiltViewModel()
            val title =
                backStackEntry.arguments?.getString(Routes.Args.SEARCH_SCREEN_QUERY).orEmpty()
            SearchScreen(
                title = title,
                searchPagingItems = viewModel.searchStateFlow.collectAsLazyPagingItems(),
                navigateToMovieDetails = {
                    navController.navigate(Screen.MovieDetailsScreen.withArgs(it))
                },
                navigateToPersonDetails = { id, name ->
                    navController.navigate(Screen.PersonDetailsScreen.withArgs(id, name))
                },
                navigateToTvShowDetails = {
                    navController.navigate(Screen.TvShowDetailsScreen.withArgs(it))
                },
                onBackPressed = {
                    navController.navigateUp()
                },
            )

            // TODO("Remove this")
//            val viewModel2: MovieDetailsViewModel = hiltViewModel()
//            Test(
//                title = title,
//                searchPagingItems = viewModel.searchStateFlow.collectAsLazyPagingItems(),
//                navigateToMovieDetails = {
//                    navController.navigate(Screen.MovieDetailsScreen.withArgs(it))
//                },
//                navigateToPersonDetails = { id, name ->
//                    navController.navigate(Screen.PersonDetailsScreen.withArgs(id, name))
//                },
//                navigateToTvShowDetails = {
//                    navController.navigate(Screen.TvShowDetailsScreen.withArgs(it))
//                },
//                onBackPressed = {
//                    navController.navigateUp()
//                },
//                contentType = contentType,
//                displayFeatures = displayFeatures,
//                uiState = viewModel2.uiState,
//                onEvent = viewModel2::onEvent
//            )
        }
        composable(
            route = "${Screen.TvShowDetailsScreen.route}${Screen.TvShowDetailsScreen.args}",
            arguments =
                listOf(
                    navArgument(Routes.Args.TV_SHOW_DETAILS_SERIES_ID) {
                        type = NavType.IntType
                        defaultValue = 0
                    },
                ),
        ) {
            val viewModel: TvShowDetailsViewModel = hiltViewModel()
            TvShowDetailsScreen(
                contentType = contentType,
                displayFeatures = displayFeatures,
                uiState = viewModel.uiState,
                onEvent = viewModel::onEvent,
                navigateToPersonDetails = { id, name ->
                    navController.navigate(Screen.PersonDetailsScreen.withArgs(id, name))
                },
                navigateToTvShowDetails = {
                    navController.navigate(Screen.TvShowDetailsScreen.withArgs(it))
                },
                onBackPressed = {
                    navController.navigateUp()
                },
            )
        }
    }
}
