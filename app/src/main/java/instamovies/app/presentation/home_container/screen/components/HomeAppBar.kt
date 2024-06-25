package instamovies.app.presentation.home_container.screen.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.material3.SearchBarDefaults
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
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
    val expanded = uiState.searchbarExpanded
    val isDocked = navigationType != InstaMoviesNavigationType.BOTTOM_NAVIGATION
    val onExpandedChange: (Boolean) -> Unit = {
        onEvent(HomeContainerUiEvent.OnSearchBarExpandedChange(it))
    }
    val content: @Composable (ColumnScope.() -> Unit) = {
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .then(
                        if (isDocked) {
                            Modifier
                        } else {
                            Modifier
                                .fillMaxSize()
                                .imeNestedScroll()
                        },
                    ),
            content = {
                if (uiState.searchQuery.isNotEmpty()) {
                    items(items = searchList, key = { it.id!! }) { result ->
                        SearchListItem(result = result) { mediaType, id, name ->
                            onEvent(HomeContainerUiEvent.OnSearch)
                            when (mediaType) {
                                MediaType.MOVIE -> navigateToMovieDetails(id)
                                MediaType.PERSON -> navigateToPersonDetails(id, name)
                                MediaType.TV -> navigateToTvShowDetails(id)
                            }
                        }
                    }
                } else {
                    items(items = trendingList, key = { it.id!! }) { result ->
                        SearchListItem(result = result) { mediaType, id, name ->
                            onEvent(HomeContainerUiEvent.OnSearch)
                            when (mediaType) {
                                MediaType.MOVIE -> navigateToMovieDetails(id)
                                MediaType.PERSON -> navigateToPersonDetails(id, name)
                                MediaType.TV -> navigateToTvShowDetails(id)
                            }
                        }
                    }
                }
                if (!isDocked) {
                    item {
                        Spacer(modifier = Modifier.navigationBarsPadding())
                    }
                }
            },
        )
    }

    if (isDocked) {
        DockedSearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = uiState.searchQuery,
                    onQueryChange = { query ->
                        onEvent(HomeContainerUiEvent.OnQueryChange(query))
                    },
                    onSearch = { query ->
                        val trimmedQuery = query.trim()

                        onEvent(HomeContainerUiEvent.OnSearch)
                        if (trimmedQuery.isNotEmpty()) {
                            navigateToSearch(trimmedQuery)
                        }
                    },
                    expanded = expanded,
                    onExpandedChange = onExpandedChange,
                    placeholder = {
                        Text(text = stringResource(id = Strings.search))
                    },
                    leadingIcon = {
                        if (expanded) {
                            IconButton(
                                onClick = {
                                    onEvent(HomeContainerUiEvent.OnSearchBarExpandedChange(false))
                                },
                            ) {
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
                            expanded && uiState.searching -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp,
                                )
                            }

                            expanded && uiState.searchQuery.isNotEmpty() -> {
                                IconButton(
                                    onClick = {
                                        onEvent(HomeContainerUiEvent.OnQueryChange(""))
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = null,
                                    )
                                }
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
                )
            },
            expanded = expanded,
            onExpandedChange = onExpandedChange,
            modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(top = 8.dp)
                    .semantics { traversalIndex = -1f },
            content = content,
        )
    } else {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = uiState.searchQuery,
                    onQueryChange = { query ->
                        onEvent(HomeContainerUiEvent.OnQueryChange(query))
                    },
                    onSearch = { query ->
                        val trimmedQuery = query.trim()

                        onEvent(HomeContainerUiEvent.OnSearch)
                        if (trimmedQuery.isNotEmpty()) {
                            navigateToSearch(trimmedQuery)
                        }
                    },
                    expanded = expanded,
                    onExpandedChange = onExpandedChange,
                    placeholder = {
                        Text(text = stringResource(id = Strings.search))
                    },
                    leadingIcon = {
                        if (expanded) {
                            IconButton(
                                onClick = {
                                    onEvent(HomeContainerUiEvent.OnSearchBarExpandedChange(false))
                                },
                            ) {
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
                            expanded -> {
                                if (uiState.searching) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        strokeWidth = 2.dp,
                                    )
                                } else {
                                    if (uiState.searchQuery.isNotEmpty()) {
                                        IconButton(
                                            onClick = {
                                                onEvent(HomeContainerUiEvent.OnQueryChange(""))
                                            },
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = null,
                                            )
                                        }
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = null,
                                        )
                                    }
                                }
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
                )
            },
            expanded = expanded,
            onExpandedChange = onExpandedChange,
            modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .semantics { traversalIndex = -1f },
            content = content,
        )
    }
}
