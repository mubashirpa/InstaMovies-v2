package instamovies.app.presentation.home_container.screen.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import instamovies.app.presentation.home_container.HomeContainerUiEvent
import instamovies.app.presentation.home_container.HomeContainerUiState
import instamovies.app.R.string as Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxScope.HomeAppBar(
    uiState: HomeContainerUiState,
    onEvent: (HomeContainerUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    isDocked: Boolean,
    onOpenNavigationDrawer: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToSearch: (query: String) -> Unit,
    content: @Composable (ColumnScope.() -> Unit),
) {
    val expanded = uiState.searchbarExpanded
    val onExpandedChange: (Boolean) -> Unit = {
        onEvent(HomeContainerUiEvent.OnSearchBarExpandedChange(it))
    }
    val searchBarModifier =
        modifier
            .align(Alignment.TopCenter)
            .semantics { traversalIndex = 0f }

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
            modifier = searchBarModifier.padding(top = 8.dp),
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
                            IconButton(onClick = onOpenNavigationDrawer) {
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
            modifier = searchBarModifier,
            content = content,
        )
    }
}
