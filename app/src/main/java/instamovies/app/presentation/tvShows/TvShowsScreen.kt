package instamovies.app.presentation.tvShows

import android.content.res.Configuration
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
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
import instamovies.app.domain.model.series.SeriesResultModel
import instamovies.app.presentation.components.Axis
import instamovies.app.presentation.components.ErrorScreen
import instamovies.app.presentation.components.LoadingIndicator
import instamovies.app.presentation.tvShows.components.TvShowGridItem
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TvShowsScreen(
    uiState: TvShowsUiState,
    onEvent: (TvShowsUiEvent) -> Unit,
    navigationType: InstaMoviesNavigationType,
    navigateToTvShowDetails: (id: Int) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val pages = TvShowsScreenPages.entries.toTypedArray()
    val pagerState = rememberPagerState { pages.size }
    val coroutineScope = rememberCoroutineScope()
    val isCompactDevice = navigationType == InstaMoviesNavigationType.BOTTOM_NAVIGATION

    Column(modifier = Modifier.fillMaxSize()) {
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

        if (isCompactDevice && configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            PrimaryScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.fillMaxWidth(),
                tabs = tabs,
            )
        } else {
            PrimaryTabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.fillMaxWidth(),
                tabs = tabs,
            )
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top,
        ) { index ->
            when (pages[index]) {
                TvShowsScreenPages.Popular -> {
                    TvShowsContent(
                        tvShowsPagingItems = uiState.popularSeries.collectAsLazyPagingItems(),
                        navigateToTvShowDetails = navigateToTvShowDetails,
                    )
                }

                TvShowsScreenPages.TopRated -> {
                    if (!uiState.isTopRatedSeriesLoaded) {
                        onEvent(TvShowsUiEvent.GetTopRatedSeries)
                    }
                    TvShowsContent(
                        tvShowsPagingItems = uiState.topRatedSeries.collectAsLazyPagingItems(),
                        navigateToTvShowDetails = navigateToTvShowDetails,
                    )
                }

                TvShowsScreenPages.OnTV -> {
                    if (!uiState.isOnTheAirSeriesLoaded) {
                        onEvent(TvShowsUiEvent.GetOnTheAirSeries)
                    }
                    TvShowsContent(
                        tvShowsPagingItems = uiState.onTheAirSeries.collectAsLazyPagingItems(),
                        navigateToTvShowDetails = navigateToTvShowDetails,
                    )
                }

                TvShowsScreenPages.AiringToday -> {
                    if (!uiState.isAiringTodaySeriesLoaded) {
                        onEvent(TvShowsUiEvent.GetAiringTodaySeries)
                    }
                    TvShowsContent(
                        tvShowsPagingItems = uiState.airingTodaySeries.collectAsLazyPagingItems(),
                        navigateToTvShowDetails = navigateToTvShowDetails,
                    )
                }
            }
        }
    }
}

@Composable
private fun TvShowsContent(
    tvShowsPagingItems: LazyPagingItems<SeriesResultModel>,
    navigateToTvShowDetails: (id: Int) -> Unit,
) {
    when (val refreshState = tvShowsPagingItems.loadState.refresh) {
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
                    tvShowsPagingItems.refresh()
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
                        count = tvShowsPagingItems.itemCount,
                        key = tvShowsPagingItems.itemKey(),
                        contentType = tvShowsPagingItems.itemContentType(),
                    ) { index ->
                        val item = tvShowsPagingItems[index]
                        if (item != null) {
                            TvShowGridItem(
                                result = item,
                                onClick = navigateToTvShowDetails,
                            )
                        }
                    }

                    when (val appendState = tvShowsPagingItems.loadState.append) {
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
                                        tvShowsPagingItems.retry()
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
