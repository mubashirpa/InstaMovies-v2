package app.instamovies.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import app.instamovies.core.util.InstaMoviesNavigationType
import app.instamovies.core.util.InstaMoviesWindowWidthType
import app.instamovies.presentation.home.HomeScreen
import app.instamovies.presentation.home.HomeViewModel
import app.instamovies.presentation.homeContainer.HomeContainerUiEvent
import app.instamovies.presentation.homeContainer.HomeContainerUiState
import app.instamovies.presentation.movies.MoviesScreen
import app.instamovies.presentation.movies.MoviesViewModel
import app.instamovies.presentation.person.PersonScreen
import app.instamovies.presentation.person.PersonViewModel
import app.instamovies.presentation.tvShows.TvShowsScreen
import app.instamovies.presentation.tvShows.TvShowsViewModel

@Composable
fun HomeNavHost(
    navController: NavHostController,
    uiState: HomeContainerUiState,
    onEvent: (HomeContainerUiEvent) -> Unit,
    navigationType: InstaMoviesNavigationType,
    windowWidthType: InstaMoviesWindowWidthType,
    onNavigateToMovieDetails: (id: Int) -> Unit,
    onNavigateToPersonDetails: (id: Int, name: String) -> Unit,
    onNavigateToTvShowDetails: (id: Int) -> Unit,
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
                onNavigateToMovieDetails = onNavigateToMovieDetails,
                onNavigateToPersonDetails = onNavigateToPersonDetails,
                onNavigateToTvShowDetails = onNavigateToTvShowDetails,
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
                onNavigateToMovieDetails = onNavigateToMovieDetails,
            )
        }
        composable<Route.Person> {
            val viewModel: PersonViewModel = hiltViewModel()
            PersonScreen(
                personPagingItems = viewModel.personStateFlow.collectAsLazyPagingItems(),
                onNavigateToPersonDetails = onNavigateToPersonDetails,
            )
        }
        composable<Route.TvShows> {
            val viewModel: TvShowsViewModel = hiltViewModel()
            TvShowsScreen(
                uiState = viewModel.uiState,
                onEvent = viewModel::onEvent,
                navigationType = navigationType,
                onNavigateToTvShowDetails = onNavigateToTvShowDetails,
            )
        }
    }
}
