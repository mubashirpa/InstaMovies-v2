package instamovies.app.presentation.home.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import instamovies.app.core.ext.carousalTransition
import instamovies.app.core.util.Resource
import instamovies.app.domain.model.MediaType
import instamovies.app.domain.model.trending.TrendingResultModel
import instamovies.app.presentation.components.Axis
import instamovies.app.presentation.components.ErrorScreen
import instamovies.app.presentation.components.LoadingIndicator
import instamovies.app.presentation.components.MediaGridItem
import instamovies.app.presentation.home.HomeUiEvent
import instamovies.app.presentation.home.HomeUiState
import instamovies.app.presentation.home.screen.components.ExploreListItem
import instamovies.app.presentation.home.screen.components.PersonListItem
import instamovies.app.R.string as Strings

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    widthSizeClass: WindowWidthSizeClass,
    trendingResource: Resource<List<TrendingResultModel>>,
    navigateToMovieDetails: (id: Int) -> Unit,
    navigateToPersonDetails: (id: Int, name: String) -> Unit,
    navigateToTvShowDetails: (id: Int) -> Unit,
    onRetry: () -> Unit,
) {
    val resources =
        listOf(
            uiState.exploreResource,
            uiState.popularMoviesResource,
            uiState.trendingPersonResource,
            trendingResource,
        )
    val isLoading = resources.all { it is Resource.Loading }
    val isError = resources.all { it is Resource.Error }
    val loading = resources.any { it is Resource.Loading }
    val error = resources.any { it is Resource.Error }

    when {
        isLoading -> {
            LoadingIndicator(modifier = Modifier.fillMaxSize())
        }

        isError -> {
            ErrorScreen(
                onRetry = {
                    onEvent(HomeUiEvent.OnRetry)
                    onRetry()
                },
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                message = trendingResource.message!!.asString(),
            )
        }

        else -> {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Explore(
                    uiState = uiState,
                    widthSizeClass = widthSizeClass,
                    navigateToMovieDetails = navigateToMovieDetails,
                )
                Trending(
                    trendingResource = trendingResource,
                    navigateToMovieDetails = navigateToMovieDetails,
                    navigateToTvShowDetails = navigateToTvShowDetails,
                )
                TrendingPerson(
                    uiState = uiState,
                    navigateToPersonDetails = navigateToPersonDetails,
                )
                Popular(
                    uiState = uiState,
                    navigateToMovieDetails = navigateToMovieDetails,
                )

                when {
                    loading -> {
                        LoadingIndicator(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 16.dp,
                                        vertical = 12.dp,
                                    ),
                        )
                    }

                    error -> {
                        ErrorScreen(
                            onRetry = {
                                onEvent(HomeUiEvent.OnRetry)
                                onRetry()
                            },
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 16.dp,
                                        vertical = 12.dp,
                                    ),
                            direction = Axis.Horizontal,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Explore(
    uiState: HomeUiState,
    widthSizeClass: WindowWidthSizeClass,
    navigateToMovieDetails: (id: Int) -> Unit,
) {
    if (uiState.exploreResource is Resource.Success) {
        val exploreList = uiState.exploreResource.data.orEmpty()
        if (exploreList.isNotEmpty()) {
            val listSize = exploreList.size
            // val pageCount = Int.MAX_VALUE TODO("Fix: java.lang.IllegalArgumentException: Cannot coerce value to an empty range: maximum -1564.0 is less than minimum 0.0.")
            val pageCount = 1000
            val maxRounds = pageCount / listSize
            val initialPage = (maxRounds / 2) * listSize
            val pagerState =
                rememberPagerState(initialPage = initialPage, pageCount = { pageCount })

            Column {
                Text(
                    text = stringResource(id = Strings.explore),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium,
                )
                when (widthSizeClass) {
                    WindowWidthSizeClass.Compact -> {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 54.dp),
                        ) {
                            val page = it % exploreList.size
                            val id = exploreList.getOrNull(page)?.id ?: 0
                            val posterPath = exploreList.getOrNull(page)?.posterPath
                            ExploreListItem(
                                posterPath = posterPath,
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(4F / 5F)
                                        .carousalTransition(it, pagerState),
                                onClick = { navigateToMovieDetails(id) },
                            )
                        }
                    }

                    else -> {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            pageSize = PageSize.Fixed(196.dp),
                            pageSpacing = 16.dp,
                        ) {
                            val page = it % exploreList.size
                            val id = exploreList.getOrNull(page)?.id ?: 0
                            val posterPath = exploreList.getOrNull(page)?.posterPath
                            ExploreListItem(
                                posterPath = posterPath,
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(4F / 5F),
                                onClick = { navigateToMovieDetails(id) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Trending(
    trendingResource: Resource<List<TrendingResultModel>>,
    navigateToMovieDetails: (id: Int) -> Unit,
    navigateToTvShowDetails: (id: Int) -> Unit,
) {
    if (trendingResource is Resource.Success) {
        val trendingList = trendingResource.data.orEmpty()
        Column {
            Text(
                text = stringResource(id = Strings.trending),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium,
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                content = {
                    items(trendingList) { result ->
                        MediaGridItem(
                            result = result,
                            onClick = { mediaType, id ->
                                when (mediaType) {
                                    MediaType.MOVIE -> navigateToMovieDetails(id)
                                    MediaType.TV -> navigateToTvShowDetails(id)
                                    else -> {}
                                }
                            },
                        )
                    }
                },
            )
        }
    }
}

@Composable
private fun TrendingPerson(
    uiState: HomeUiState,
    navigateToPersonDetails: (id: Int, name: String) -> Unit,
) {
    if (uiState.trendingPersonResource is Resource.Success) {
        val trendingList = uiState.trendingPersonResource.data.orEmpty()
        Column {
            Text(
                text = stringResource(id = Strings.label_trending_people),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium,
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                content = {
                    items(trendingList) { result ->
                        PersonListItem(
                            result = result,
                            onClick = navigateToPersonDetails,
                        )
                    }
                },
            )
        }
    }
}

@Composable
private fun Popular(
    uiState: HomeUiState,
    navigateToMovieDetails: (id: Int) -> Unit,
) {
    if (uiState.popularMoviesResource is Resource.Success) {
        val popularList = uiState.popularMoviesResource.data.orEmpty()
        Column {
            Text(
                text = stringResource(id = Strings.popular),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium,
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                content = {
                    items(popularList) { result ->
                        MediaGridItem(
                            result = result,
                            onClick = navigateToMovieDetails,
                        )
                    }
                },
            )
        }
    }
}
