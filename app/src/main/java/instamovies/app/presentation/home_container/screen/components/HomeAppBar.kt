package instamovies.app.presentation.home_container.screen.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import instamovies.app.core.util.InstaMoviesNavigationType
import instamovies.app.domain.model.MediaType
import instamovies.app.domain.model.trending.TrendingResultModel
import instamovies.app.presentation.home_container.HomeContainerUiEvent
import instamovies.app.presentation.home_container.HomeContainerUiState
import instamovies.app.R.string as Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxScope.HomeAppBar(
    uiState: HomeContainerUiState,
    onEvent: (HomeContainerUiEvent) -> Unit,
    navigationType: InstaMoviesNavigationType,
    trendingList: List<TrendingResultModel>,
    onMenuIconClick: () -> Unit,
    navigateToMovieDetails: (id: Int) -> Unit,
    navigateToPersonDetails: (id: Int, name: String) -> Unit,
    navigateToProfile: () -> Unit,
    navigateToSearch: (query: String) -> Unit,
    navigateToTvShowDetails: (id: Int) -> Unit,
) {
    val searchList = uiState.searchResource.data.orEmpty()
    val isActive = uiState.searchbarActive
    val isDocked = navigationType != InstaMoviesNavigationType.BOTTOM_NAVIGATION
    val content: @Composable (ColumnScope.() -> Unit) = {
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .then(
                        if (isDocked) {
                            Modifier
                        } else {
                            Modifier.navigationBarsPadding()
                        },
                    ),
            content = {
                if (uiState.searchText.isNotEmpty()) {
                    items(searchList) { result ->
                        SearchListItem(result = result) { mediaType, id, name ->
                            onEvent(HomeContainerUiEvent.OnSearch)
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
                        }
                    }
                } else {
                    items(trendingList) { result ->
                        SearchListItem(result = result) { mediaType, id, name ->
                            onEvent(HomeContainerUiEvent.OnSearch)
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
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(8.dp)) }
            },
        )
    }

    if (isDocked) {
        DockedSearchBar(
            query = uiState.searchText,
            onQueryChange = { onEvent(HomeContainerUiEvent.OnSearchTextChange(it)) },
            onSearch = {
                onEvent(HomeContainerUiEvent.OnSearch)
                if (it.isNotEmpty()) {
                    navigateToSearch(it)
                }
            },
            active = isActive,
            onActiveChange = { onEvent(HomeContainerUiEvent.OnSearchBarActiveChange(it)) },
            modifier =
                Modifier
                    .align(Alignment.TopStart)
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
                    .statusBarsPadding()
                    .semantics { traversalIndex = -1f }
                    .then(if (navigationType == InstaMoviesNavigationType.NAVIGATION_RAIL) Modifier.fillMaxWidth() else Modifier),
            placeholder = { Text(text = stringResource(id = Strings.search)) },
            leadingIcon = {
                if (isActive) {
                    IconButton(onClick = {
                        onEvent(
                            HomeContainerUiEvent.OnSearchBarActiveChange(
                                false,
                            ),
                        )
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                    )
                }
            },
            trailingIcon = {
                when {
                    isActive && uiState.searching -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp,
                        )
                    }

                    isActive && uiState.searchText.isNotEmpty() -> {
                        IconButton(onClick = { onEvent(HomeContainerUiEvent.OnSearchTextChange("")) }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null,
                            )
                        }
                    }

                    isActive -> {}

                    else -> {
                        IconButton(onClick = navigateToProfile) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = null,
                            )
                        }
                    }
                }
            },
            content = content,
        )
    } else {
        SearchBar(
            query = uiState.searchText,
            onQueryChange = { onEvent(HomeContainerUiEvent.OnSearchTextChange(it)) },
            onSearch = {
                onEvent(HomeContainerUiEvent.OnSearch)
                if (it.isNotEmpty()) {
                    navigateToSearch(it)
                }
            },
            active = isActive,
            onActiveChange = { onEvent(HomeContainerUiEvent.OnSearchBarActiveChange(it)) },
            modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .semantics { traversalIndex = -1f },
            placeholder = { Text(text = stringResource(id = Strings.search)) },
            leadingIcon = {
                if (isActive) {
                    IconButton(onClick = {
                        onEvent(
                            HomeContainerUiEvent.OnSearchBarActiveChange(
                                false,
                            ),
                        )
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                } else {
                    IconButton(onClick = onMenuIconClick) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                    }
                }
            },
            trailingIcon = {
                when {
                    isActive && uiState.searching -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp,
                        )
                    }

                    isActive && uiState.searchText.isNotEmpty() -> {
                        IconButton(onClick = { onEvent(HomeContainerUiEvent.OnSearchTextChange("")) }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null,
                            )
                        }
                    }

                    isActive -> {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                        )
                    }

                    else -> {
                        IconButton(onClick = navigateToProfile) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = null,
                            )
                        }
                    }
                }
            },
            content = content,
        )
    }
}
