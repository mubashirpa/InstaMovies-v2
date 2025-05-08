package instamovies.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.window.layout.DisplayFeature
import instamovies.app.core.util.InstaMoviesContentType
import instamovies.app.core.util.InstaMoviesWindowWidthType
import instamovies.app.presentation.homeContainer.HomeContainerScreen
import instamovies.app.presentation.homeContainer.HomeContainerViewModel
import instamovies.app.presentation.movieDetails.MovieDetailsScreen
import instamovies.app.presentation.movieDetails.MovieDetailsViewModel
import instamovies.app.presentation.personDetails.PersonDetailsScreen
import instamovies.app.presentation.personDetails.PersonDetailsViewModel
import instamovies.app.presentation.search.SearchScreen
import instamovies.app.presentation.search.SearchViewModel
import instamovies.app.presentation.tvShowDetails.TvShowDetailsScreen
import instamovies.app.presentation.tvShowDetails.TvShowDetailsViewModel

@Composable
fun InstaMoviesNavHost(
    navController: NavHostController,
    contentType: InstaMoviesContentType,
    windowWidthType: InstaMoviesWindowWidthType,
    displayFeatures: List<DisplayFeature>,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Route.HomeContainer,
        modifier = modifier,
    ) {
        composable<Route.HomeContainer> {
            val viewModel: HomeContainerViewModel = hiltViewModel()
            HomeContainerScreen(
                uiState = viewModel.uiState,
                onEvent = viewModel::onEvent,
                windowWidthType = windowWidthType,
                onNavigateToMovieDetails = { id ->
                    navController.navigate(Route.MovieDetails(movieId = id))
                },
                onNavigateToPersonDetails = { id, name ->
                    navController.navigate(Route.PersonDetails(personId = id, personName = name))
                },
                onNavigateToSearch = {
                    navController.navigate(Route.Search(searchQuery = it))
                },
                onNavigateToTvShowDetails = { id ->
                    navController.navigate(Route.TvShowDetails(seriesId = id))
                },
            )
        }
        composable<Route.MovieDetails> {
            val viewModel: MovieDetailsViewModel = hiltViewModel()
            MovieDetailsScreen(
                contentType = contentType,
                displayFeatures = displayFeatures,
                uiState = viewModel.uiState,
                onEvent = viewModel::onEvent,
                navigateToMovieDetails = { id ->
                    navController.navigate(Route.MovieDetails(movieId = id))
                },
                navigateToPersonDetails = { id, name ->
                    navController.navigate(Route.PersonDetails(personId = id, personName = name))
                },
                onBackPressed = navController::navigateUp,
            )
        }
        composable<Route.PersonDetails> { backStackEntry ->
            val viewModel: PersonDetailsViewModel = hiltViewModel()
            val personDetails = backStackEntry.toRoute<Route.PersonDetails>()

            PersonDetailsScreen(
                contentType = contentType,
                displayFeatures = displayFeatures,
                uiState = viewModel.uiState,
                onEvent = viewModel::onEvent,
                title = personDetails.personName,
                navigateToMovieDetails = { id ->
                    navController.navigate(Route.MovieDetails(movieId = id))
                },
                navigateToTvShowDetails = { id ->
                    navController.navigate(Route.TvShowDetails(seriesId = id))
                },
                onBackPressed = navController::navigateUp,
            )
        }
        composable<Route.Search> { backStackEntry ->
            val viewModel: SearchViewModel = hiltViewModel()
            val search = backStackEntry.toRoute<Route.Search>()

            SearchScreen(
                title = search.searchQuery,
                searchPagingItems = viewModel.searchStateFlow.collectAsLazyPagingItems(),
                navigateToMovieDetails = { id ->
                    navController.navigate(Route.MovieDetails(movieId = id))
                },
                navigateToPersonDetails = { id, name ->
                    navController.navigate(Route.PersonDetails(personId = id, personName = name))
                },
                navigateToTvShowDetails = { id ->
                    navController.navigate(Route.TvShowDetails(seriesId = id))
                },
                onBackPressed = navController::navigateUp,
            )
        }
        composable<Route.TvShowDetails> {
            val viewModel: TvShowDetailsViewModel = hiltViewModel()
            TvShowDetailsScreen(
                contentType = contentType,
                displayFeatures = displayFeatures,
                uiState = viewModel.uiState,
                onEvent = viewModel::onEvent,
                navigateToPersonDetails = { id, name ->
                    navController.navigate(Route.PersonDetails(personId = id, personName = name))
                },
                navigateToTvShowDetails = { id ->
                    navController.navigate(Route.TvShowDetails(seriesId = id))
                },
                onBackPressed = navController::navigateUp,
            )
        }
    }
}
