package app.instamovies.presentation.person

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import app.instamovies.R
import app.instamovies.core.ext.header
import app.instamovies.domain.model.person.popular.PersonResultModel
import app.instamovies.presentation.components.Axis
import app.instamovies.presentation.components.ErrorScreen
import app.instamovies.presentation.components.LoadingScreen
import app.instamovies.presentation.person.components.PersonGridItem
import retrofit2.HttpException
import java.io.IOException

@Composable
fun PersonScreen(
    personPagingItems: LazyPagingItems<PersonResultModel>,
    onNavigateToPersonDetails: (id: Int, name: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .windowInsetsPadding(
                    WindowInsets.systemBars
                        .union(WindowInsets.displayCutout)
                        .only(WindowInsetsSides.Horizontal),
                ),
    ) {
        when (val refreshState = personPagingItems.loadState.refresh) {
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
                        personPagingItems.refresh()
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
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(150.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    content = {
                        header {
                            Text(
                                text = stringResource(id = R.string.label_popular_people),
                                modifier = Modifier.padding(top = 4.dp, bottom = 6.dp),
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                        items(
                            count = personPagingItems.itemCount,
                            key = personPagingItems.itemKey(),
                            contentType = personPagingItems.itemContentType(),
                        ) { index ->
                            val item = personPagingItems[index]
                            if (item != null) {
                                PersonGridItem(result = item, onClick = onNavigateToPersonDetails)
                            }
                        }

                        when (val appendState = personPagingItems.loadState.append) {
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
                                            personPagingItems.retry()
                                        },
                                        modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp)
                                                .padding(top = 14.dp, bottom = 12.dp),
                                        direction = Axis.Horizontal,
                                        message = message,
                                    )
                                }
                            }

                            LoadState.Loading -> {
                                header {
                                    LoadingScreen(
                                        modifier =
                                            Modifier
                                                .padding(horizontal = 16.dp)
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
}
