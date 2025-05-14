package app.instamovies.presentation.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import app.instamovies.domain.model.MediaType
import app.instamovies.domain.model.search.SearchResultModel
import app.instamovies.presentation.components.BackButton
import app.instamovies.presentation.components.ErrorScreen
import app.instamovies.presentation.components.LoadingScreen
import app.instamovies.presentation.search.components.SearchListItem
import retrofit2.HttpException
import java.io.IOException
import app.instamovies.R.string as Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    title: String,
    searchPagingItems: LazyPagingItems<SearchResultModel>,
    navigateToMovieDetails: (id: Int) -> Unit,
    navigateToPersonDetails: (id: Int, name: String) -> Unit,
    navigateToTvShowDetails: (id: Int) -> Unit,
    onBackPressed: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = title, overflow = TextOverflow.Ellipsis, maxLines = 1) },
                navigationIcon = { BackButton(onClick = onBackPressed) },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        when (val refreshState = searchPagingItems.loadState.refresh) {
            is LoadState.Error -> {
                val message =
                    when (refreshState.error) {
                        is IOException -> {
                            stringResource(id = Strings.error_no_internet)
                        }

                        is HttpException -> {
                            stringResource(id = Strings.error_unexpected)
                        }

                        else -> {
                            stringResource(id = Strings.error_unknown)
                        }
                    }
                ErrorScreen(
                    onRetry = {
                        searchPagingItems.refresh()
                    },
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                    message = message,
                )
            }

            LoadState.Loading -> {
                LoadingScreen()
            }

            is LoadState.NotLoading -> {
                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                    contentPadding = innerPadding,
                    content = {
                        items(
                            count = searchPagingItems.itemCount,
                            key = searchPagingItems.itemKey(),
                            contentType = searchPagingItems.itemContentType(),
                        ) { index ->
                            val item = searchPagingItems[index]
                            if (item != null) {
                                SearchListItem(
                                    result = item,
                                    onClick = { mediaType, id, name ->
                                        when (mediaType) {
                                            MediaType.MOVIE -> {
                                                navigateToMovieDetails(id)
                                            }

                                            MediaType.PERSON -> {
                                                navigateToPersonDetails(id, name)
                                            }

                                            MediaType.TV -> {
                                                navigateToTvShowDetails(id)
                                            }
                                        }
                                    },
                                )
                            }
                        }

                        when (val appendState = searchPagingItems.loadState.append) {
                            is LoadState.Error -> {
                                item {
                                    val message =
                                        when (appendState.error) {
                                            is IOException -> {
                                                stringResource(id = Strings.error_no_internet)
                                            }

                                            is HttpException -> {
                                                stringResource(id = Strings.error_unexpected)
                                            }

                                            else -> {
                                                stringResource(id = Strings.error_unknown)
                                            }
                                        }
                                    ErrorScreen(
                                        onRetry = {
                                            searchPagingItems.retry()
                                        },
                                        modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp, vertical = 12.dp),
                                        message = message,
                                    )
                                }
                            }

                            LoadState.Loading -> {
                                item {
                                    LoadingScreen(
                                        modifier =
                                            Modifier
                                                .padding(
                                                    horizontal = 16.dp,
                                                    vertical = 12.dp,
                                                ),
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
}
