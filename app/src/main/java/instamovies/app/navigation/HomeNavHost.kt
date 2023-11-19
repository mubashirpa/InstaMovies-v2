package instamovies.app.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import instamovies.app.core.util.InstaMoviesNavigationType
import instamovies.app.presentation.home.HomeViewModel
import instamovies.app.presentation.home.screen.HomeScreen
import instamovies.app.presentation.home_container.HomeContainerUiEvent
import instamovies.app.presentation.home_container.HomeContainerUiState
import instamovies.app.presentation.movies.MoviesViewModel
import instamovies.app.presentation.movies.screen.MoviesScreen
import instamovies.app.presentation.person.PersonViewModel
import instamovies.app.presentation.person.screen.PersonScreen
import instamovies.app.presentation.tv_shows.TvShowsViewModel
import instamovies.app.presentation.tv_shows.screen.TvShowsScreen

@Composable
fun HomeNavHost(
    navController: NavHostController,
    uiState: HomeContainerUiState,
    onEvent: (HomeContainerUiEvent) -> Unit,
    widthSizeClass: WindowWidthSizeClass,
    navigationType: InstaMoviesNavigationType,
    modifier: Modifier = Modifier,
    navigateToMovieDetails: (id: Int) -> Unit,
    navigateToPersonDetails: (id: Int, name: String) -> Unit,
    navigateToTvShowDetails: (id: Int) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route,
        modifier = modifier,
    ) {
        composable(route = Screen.HomeScreen.route) {
            val viewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                uiState = viewModel.uiState,
                onEvent = viewModel::onEvent,
                widthSizeClass = widthSizeClass,
                trendingResource = uiState.trendingResource,
                navigateToMovieDetails = navigateToMovieDetails,
                navigateToPersonDetails = navigateToPersonDetails,
                navigateToTvShowDetails = navigateToTvShowDetails,
                onRetry = {
                    onEvent(HomeContainerUiEvent.OnRetry)
                },
            )
        }
        composable(route = Screen.MoviesScreen.route) {
            val viewModel: MoviesViewModel = hiltViewModel()
            MoviesScreen(
                uiState = viewModel.uiState,
                onEvent = viewModel::onEvent,
                navigationType = navigationType,
                navigateToMovieDetails = navigateToMovieDetails,
            )
        }
        composable(route = Screen.PersonScreen.route) {
            val viewModel: PersonViewModel = hiltViewModel()
            PersonScreen(
                personPagingItems = viewModel.personStateFlow.collectAsLazyPagingItems(),
                navigateToPersonDetails = navigateToPersonDetails,
            )
        }
        composable(route = Screen.TvShowsScreen.route) {
            val viewModel: TvShowsViewModel = hiltViewModel()
            TvShowsScreen(
                uiState = viewModel.uiState,
                onEvent = viewModel::onEvent,
                navigationType = navigationType,
                navigateToTvShowDetails = navigateToTvShowDetails,
            )
        }
    }
}
