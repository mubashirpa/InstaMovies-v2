package instamovies.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import instamovies.app.core.util.InstaMoviesNavigationType
import instamovies.app.core.util.InstaMoviesWindowWidthType
import instamovies.app.presentation.home.HomeScreen
import instamovies.app.presentation.home.HomeViewModel
import instamovies.app.presentation.homeContainer.HomeContainerUiEvent
import instamovies.app.presentation.homeContainer.HomeContainerUiState
import instamovies.app.presentation.movies.MoviesScreen
import instamovies.app.presentation.movies.MoviesViewModel
import instamovies.app.presentation.person.PersonScreen
import instamovies.app.presentation.person.PersonViewModel
import instamovies.app.presentation.tvShows.TvShowsScreen
import instamovies.app.presentation.tvShows.TvShowsViewModel

@Composable
fun HomeNavHost(
    navController: NavHostController,
    uiState: HomeContainerUiState,
    onEvent: (HomeContainerUiEvent) -> Unit,
    navigationType: InstaMoviesNavigationType,
    windowWidthType: InstaMoviesWindowWidthType,
    navigateToMovieDetails: (id: Int) -> Unit,
    navigateToPersonDetails: (id: Int, name: String) -> Unit,
    navigateToTvShowDetails: (id: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Route.Home,
        modifier = modifier,
    ) {
        composable<Route.Home> {
            val viewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                uiState = viewModel.uiState,
                onEvent = viewModel::onEvent,
                windowWidthType = windowWidthType,
                trendingResource = uiState.trendingResource,
                navigateToMovieDetails = navigateToMovieDetails,
                navigateToPersonDetails = navigateToPersonDetails,
                navigateToTvShowDetails = navigateToTvShowDetails,
                onRetry = {
                    onEvent(HomeContainerUiEvent.Retry)
                },
            )
        }
        composable<Route.Movies> {
            val viewModel: MoviesViewModel = hiltViewModel()
            MoviesScreen(
                uiState = viewModel.uiState,
                onEvent = viewModel::onEvent,
                navigationType = navigationType,
                navigateToMovieDetails = navigateToMovieDetails,
            )
        }
        composable<Route.Person> {
            val viewModel: PersonViewModel = hiltViewModel()
            PersonScreen(
                personPagingItems = viewModel.personStateFlow.collectAsLazyPagingItems(),
                navigateToPersonDetails = navigateToPersonDetails,
            )
        }
        composable<Route.TvShows> {
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
