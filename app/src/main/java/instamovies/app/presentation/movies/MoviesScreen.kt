package instamovies.app.presentation.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import instamovies.app.R
import instamovies.app.core.ext.header
import instamovies.app.core.util.InstaMoviesNavigationType
import instamovies.app.domain.model.movie.MovieResultModel
import instamovies.app.presentation.components.Axis
import instamovies.app.presentation.components.ErrorScreen
import instamovies.app.presentation.components.LoadingIndicator
import instamovies.app.presentation.movies.components.MovieGridItem
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

private val ScrollableTabRowPadding = 16.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(
    uiState: MoviesUiState,
    onEvent: (MoviesUiEvent) -> Unit,
    navigationType: InstaMoviesNavigationType,
    navigateToMovieDetails: (id: Int) -> Unit,
) {
    val pages = MoviesScreenPages.entries.toTypedArray()
    val pagerState = rememberPagerState { pages.size }
    val coroutineScope = rememberCoroutineScope()
    val isCompactDevice = navigationType == InstaMoviesNavigationType.BOTTOM_NAVIGATION

    Column(modifier = Modifier.fillMaxSize()) {
        PrimaryScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth(),
            edgePadding = if (isCompactDevice) ScrollableTabRowPadding else 0.dp,
            divider = {
                if (isCompactDevice) {
                    HorizontalDivider()
                }
            },
        ) {
            pages.forEachIndexed { index, page ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.scrollToPage(index)
                        }
                    },
                    text = { Text(text = stringResource(id = page.titleResId)) },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top,
        ) { index ->
            when (pages[index]) {
                MoviesScreenPages.Popular -> {
                    MoviesContent(
                        moviesPagingItems = uiState.popularMovies.collectAsLazyPagingItems(),
                        navigateToMovieDetails = navigateToMovieDetails,
                    )
                }

                MoviesScreenPages.TopRated -> {
                    if (!uiState.isTopRatedMoviesLoaded) {
                        onEvent(MoviesUiEvent.GetTopRatedMovies)
                    }
                    MoviesContent(
                        moviesPagingItems = uiState.topRatedMovies.collectAsLazyPagingItems(),
                        navigateToMovieDetails = navigateToMovieDetails,
                    )
                }

                MoviesScreenPages.Upcoming -> {
                    if (!uiState.isUpcomingMoviesLoaded) {
                        onEvent(MoviesUiEvent.GetUpcomingMovies)
                    }
                    MoviesContent(
                        moviesPagingItems = uiState.upcomingMovies.collectAsLazyPagingItems(),
                        navigateToMovieDetails = navigateToMovieDetails,
                    )
                }

                MoviesScreenPages.NowPlaying -> {
                    if (!uiState.isNowPlayingMoviesLoaded) {
                        onEvent(MoviesUiEvent.GetNowPlayingMovies)
                    }
                    MoviesContent(
                        moviesPagingItems = uiState.nowPlayingMovies.collectAsLazyPagingItems(),
                        navigateToMovieDetails = navigateToMovieDetails,
                    )
                }
            }
        }
    }
}

@Composable
private fun MoviesContent(
    moviesPagingItems: LazyPagingItems<MovieResultModel>,
    navigateToMovieDetails: (id: Int) -> Unit,
) {
    when (val refreshState = moviesPagingItems.loadState.refresh) {
        is LoadState.Error -> {
            val message =
                when (refreshState.error) {
                    is IOException -> {
                        stringResource(id = R.string.error_no_internet)
                    }

                    is HttpException -> {
                        stringResource(id = R.string.error_unexpected)
                    }

                    else -> {
                        stringResource(id = R.string.error_unknown)
                    }
                }
            ErrorScreen(
                onRetry = {
                    moviesPagingItems.refresh()
                },
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                message = message,
            )
        }

        LoadState.Loading -> {
            LoadingIndicator(modifier = Modifier.fillMaxSize())
        }

        is LoadState.NotLoading -> {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(100.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding =
                    PaddingValues(
                        horizontal = 16.dp,
                        vertical = 12.dp,
                    ),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                content = {
                    items(
                        count = moviesPagingItems.itemCount,
                        key = moviesPagingItems.itemKey(),
                        contentType = moviesPagingItems.itemContentType(),
                    ) { index ->
                        val item = moviesPagingItems[index]
                        if (item != null) {
                            MovieGridItem(
                                result = item,
                                onClick = navigateToMovieDetails,
                            )
                        }
                    }

                    when (val appendState = moviesPagingItems.loadState.append) {
                        is LoadState.Error -> {
                            header {
                                val message =
                                    when (appendState.error) {
                                        is IOException -> {
                                            stringResource(id = R.string.error_no_internet)
                                        }

                                        is HttpException -> {
                                            stringResource(id = R.string.error_unexpected)
                                        }

                                        else -> {
                                            stringResource(id = R.string.error_unknown)
                                        }
                                    }
                                ErrorScreen(
                                    onRetry = {
                                        moviesPagingItems.retry()
                                    },
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(top = 14.dp, bottom = 12.dp),
                                    direction = Axis.Horizontal,
                                    message = message,
                                )
                            }
                        }

                        LoadState.Loading -> {
                            header {
                                LoadingIndicator(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .fillMaxWidth()
                                            .padding(top = 14.dp, bottom = 12.dp),
                                )
                            }
                        }

                        is LoadState.NotLoading -> {}
                    }
                },
            )
        }
    }
}
