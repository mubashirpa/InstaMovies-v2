package instamovies.app.presentation.movies

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
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

@Composable
fun MoviesScreen(
    uiState: MoviesUiState,
    onEvent: (MoviesUiEvent) -> Unit,
    navigationType: InstaMoviesNavigationType,
    onNavigateToMovieDetails: (id: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current
    val pages = MoviesScreenPages.entries.toTypedArray()
    val pagerState = rememberPagerState { pages.size }
    val coroutineScope = rememberCoroutineScope()
    val isCompactDevice = navigationType == InstaMoviesNavigationType.BOTTOM_NAVIGATION

    Scaffold(
        modifier = modifier,
        topBar = {
            val tabs: @Composable () -> Unit = {
                pages.forEachIndexed { index, page ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.scrollToPage(index)
                            }
                        },
                        text = {
                            Text(text = stringResource(id = page.titleResId))
                        },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            val tabRowModifier =
                Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(
                        WindowInsets.systemBars
                            .union(WindowInsets.displayCutout)
                            .only(WindowInsetsSides.Top),
                    )

            Box(modifier = Modifier.background(TabRowDefaults.primaryContainerColor)) {
                if (isCompactDevice && configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    PrimaryScrollableTabRow(
                        selectedTabIndex = pagerState.currentPage,
                        modifier = tabRowModifier,
                        tabs = tabs,
                    )
                } else {
                    PrimaryTabRow(
                        selectedTabIndex = pagerState.currentPage,
                        modifier = tabRowModifier,
                        tabs = tabs,
                    )
                }
            }
        },
        contentWindowInsets =
            WindowInsets.systemBars
                .union(WindowInsets.displayCutout)
                .exclude(WindowInsets.navigationBars),
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top,
        ) { index ->
            when (pages[index]) {
                MoviesScreenPages.Popular -> {
                    MoviesContent(
                        moviesPagingItems = uiState.popularMovies.collectAsLazyPagingItems(),
                        innerPadding = innerPadding,
                        onNavigateToMovieDetails = onNavigateToMovieDetails,
                    )
                }

                MoviesScreenPages.TopRated -> {
                    if (!uiState.isTopRatedMoviesLoaded) {
                        onEvent(MoviesUiEvent.GetTopRatedMovies)
                    }
                    MoviesContent(
                        moviesPagingItems = uiState.topRatedMovies.collectAsLazyPagingItems(),
                        innerPadding = innerPadding,
                        onNavigateToMovieDetails = onNavigateToMovieDetails,
                    )
                }

                MoviesScreenPages.Upcoming -> {
                    if (!uiState.isUpcomingMoviesLoaded) {
                        onEvent(MoviesUiEvent.GetUpcomingMovies)
                    }
                    MoviesContent(
                        moviesPagingItems = uiState.upcomingMovies.collectAsLazyPagingItems(),
                        innerPadding = innerPadding,
                        onNavigateToMovieDetails = onNavigateToMovieDetails,
                    )
                }

                MoviesScreenPages.NowPlaying -> {
                    if (!uiState.isNowPlayingMoviesLoaded) {
                        onEvent(MoviesUiEvent.GetNowPlayingMovies)
                    }
                    MoviesContent(
                        moviesPagingItems = uiState.nowPlayingMovies.collectAsLazyPagingItems(),
                        innerPadding = innerPadding,
                        onNavigateToMovieDetails = onNavigateToMovieDetails,
                    )
                }
            }
        }
    }
}

@Composable
private fun MoviesContent(
    moviesPagingItems: LazyPagingItems<MovieResultModel>,
    innerPadding: PaddingValues,
    onNavigateToMovieDetails: (id: Int) -> Unit,
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
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                message = message,
            )
        }

        LoadState.Loading -> {
            LoadingIndicator(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
            )
        }

        is LoadState.NotLoading -> {
            val layoutDirection = LocalLayoutDirection.current
            val contentPadding =
                PaddingValues(
                    start = innerPadding.calculateStartPadding(layoutDirection).plus(16.dp),
                    top = innerPadding.calculateTopPadding().plus(12.dp),
                    end = innerPadding.calculateEndPadding(layoutDirection).plus(16.dp),
                    bottom = innerPadding.calculateBottomPadding().plus(12.dp),
                )

            LazyVerticalGrid(
                columns = GridCells.Adaptive(100.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = contentPadding,
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
                                onClick = onNavigateToMovieDetails,
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
