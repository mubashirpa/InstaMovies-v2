package app.instamovies.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.instamovies.core.ext.carousalTransition
import app.instamovies.core.util.InstaMoviesWindowWidthType
import app.instamovies.core.util.Resource
import app.instamovies.domain.model.MediaType
import app.instamovies.domain.model.trending.TrendingResultModel
import app.instamovies.presentation.components.Axis
import app.instamovies.presentation.components.ErrorScreen
import app.instamovies.presentation.components.LoadingScreen
import app.instamovies.presentation.components.MediaGridItem
import app.instamovies.presentation.home.components.ExploreListItem
import app.instamovies.presentation.home.components.PersonListItem
import app.instamovies.R.string as Strings

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    windowWidthType: InstaMoviesWindowWidthType,
    trendingResource: Resource<List<TrendingResultModel>>,
    onNavigateToMovieDetails: (id: Int) -> Unit,
    onNavigateToPersonDetails: (id: Int, name: String) -> Unit,
    onNavigateToTvShowDetails: (id: Int) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
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

    Scaffold(modifier = modifier) { innerPadding ->
        when {
            isLoading -> {
                LoadingScreen(modifier = Modifier.padding(innerPadding))
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
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                    message = trendingResource.message!!.asString(),
                )
            }

            else -> {
                val layoutDirection = LocalLayoutDirection.current
                val contentHorizontalPadding =
                    PaddingValues(
                        start = innerPadding.calculateStartPadding(layoutDirection).plus(16.dp),
                        end = innerPadding.calculateEndPadding(layoutDirection).plus(16.dp),
                    )

                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Explore(
                        uiState = uiState,
                        windowWidthType = windowWidthType,
                        contentPadding = contentHorizontalPadding,
                        onNavigateToMovieDetails = onNavigateToMovieDetails,
                    )
                    Trending(
                        trendingResource = trendingResource,
                        contentPadding = contentHorizontalPadding,
                        onNavigateToMovieDetails = onNavigateToMovieDetails,
                        onNavigateToTvShowDetails = onNavigateToTvShowDetails,
                    )
                    TrendingPerson(
                        uiState = uiState,
                        contentPadding = contentHorizontalPadding,
                        onNavigateToPersonDetails = onNavigateToPersonDetails,
                    )
                    Popular(
                        uiState = uiState,
                        contentPadding = contentHorizontalPadding,
                        onNavigateToMovieDetails = onNavigateToMovieDetails,
                    )

                    when {
                        loading -> {
                            LoadingScreen(
                                modifier =
                                    Modifier
                                        .padding(contentHorizontalPadding)
                                        .padding(vertical = 12.dp),
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
                                        .padding(contentHorizontalPadding)
                                        .padding(vertical = 12.dp),
                                direction = Axis.Horizontal,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun Explore(
    uiState: HomeUiState,
    windowWidthType: InstaMoviesWindowWidthType,
    contentPadding: PaddingValues,
    onNavigateToMovieDetails: (id: Int) -> Unit,
) {
    if (uiState.exploreResource is Resource.Success) {
        val exploreList = uiState.exploreResource.data.orEmpty()
        if (exploreList.isNotEmpty()) {
            val listSize = exploreList.size
            val pageCount = Int.MAX_VALUE
            val maxRounds = pageCount / listSize
            val initialPage = (maxRounds / 2) * listSize
            val pagerState =
                rememberPagerState(initialPage = initialPage, pageCount = { pageCount })

            Column {
                Text(
                    text = stringResource(id = Strings.explore),
                    modifier =
                        Modifier
                            .padding(contentPadding)
                            .padding(vertical = 16.dp),
                    style = MaterialTheme.typography.titleMedium,
                )
                when (windowWidthType) {
                    InstaMoviesWindowWidthType.COMPACT -> {
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
                                onClick = { onNavigateToMovieDetails(id) },
                            )
                        }
                    }

                    else -> {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = contentPadding,
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
                                onClick = { onNavigateToMovieDetails(id) },
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
    contentPadding: PaddingValues,
    onNavigateToMovieDetails: (id: Int) -> Unit,
    onNavigateToTvShowDetails: (id: Int) -> Unit,
) {
    if (trendingResource is Resource.Success) {
        val trendingList = trendingResource.data.orEmpty()

        Column {
            Text(
                text = stringResource(id = Strings.trending),
                modifier =
                    Modifier
                        .padding(contentPadding)
                        .padding(vertical = 16.dp),
                style = MaterialTheme.typography.titleMedium,
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = contentPadding,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                content = {
                    items(trendingList) { result ->
                        MediaGridItem(
                            result = result,
                            onClick = { mediaType, id ->
                                when (mediaType) {
                                    MediaType.MOVIE -> onNavigateToMovieDetails(id)
                                    MediaType.TV -> onNavigateToTvShowDetails(id)
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
    contentPadding: PaddingValues,
    onNavigateToPersonDetails: (id: Int, name: String) -> Unit,
) {
    if (uiState.trendingPersonResource is Resource.Success) {
        val trendingList = uiState.trendingPersonResource.data.orEmpty()
        Column {
            Text(
                text = stringResource(id = Strings.label_trending_people),
                modifier =
                    Modifier
                        .padding(contentPadding)
                        .padding(vertical = 16.dp),
                style = MaterialTheme.typography.titleMedium,
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = contentPadding,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                content = {
                    items(trendingList) { result ->
                        PersonListItem(
                            result = result,
                            onClick = onNavigateToPersonDetails,
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
    contentPadding: PaddingValues,
    onNavigateToMovieDetails: (id: Int) -> Unit,
) {
    if (uiState.popularMoviesResource is Resource.Success) {
        val popularList = uiState.popularMoviesResource.data.orEmpty()
        Column {
            Text(
                text = stringResource(id = Strings.popular),
                modifier =
                    Modifier
                        .padding(contentPadding)
                        .padding(vertical = 16.dp),
                style = MaterialTheme.typography.titleMedium,
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = contentPadding,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                content = {
                    items(popularList) { result ->
                        MediaGridItem(
                            result = result,
                            onClick = onNavigateToMovieDetails,
                        )
                    }
                },
            )
        }
    }
}
