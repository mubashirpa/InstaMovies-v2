package instamovies.app.presentation.homeContainer.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExpandedDockedSearchBar
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarScrollBehavior
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopSearchBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import instamovies.app.presentation.homeContainer.HomeContainerUiState
import kotlinx.coroutines.launch
import instamovies.app.R.string as Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(
    uiState: HomeContainerUiState,
    searchBarState: SearchBarState,
    isDocked: Boolean,
    scrollBehavior: SearchBarScrollBehavior,
    onNavigationIconClick: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToSearch: (query: String) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit),
) {
    val coroutineScope = rememberCoroutineScope()
    val expanded = searchBarState.currentValue == SearchBarValue.Expanded

    val inputField =
        @Composable {
            SearchBarDefaults.InputField(
                searchBarState = searchBarState,
                textFieldState = uiState.searchQueryState,
                onSearch = { query ->
                    coroutineScope
                        .launch {
                            searchBarState.animateToCollapsed()
                        }.invokeOnCompletion {
                            val trimmedQuery = query.trim()
                            if (trimmedQuery.isNotEmpty()) {
                                onNavigateToSearch(trimmedQuery)
                            }
                        }
                },
                placeholder = {
                    Text(text = stringResource(id = Strings.search))
                },
                leadingIcon = {
                    when {
                        expanded -> {
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        searchBarState.animateToCollapsed()
                                    }
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null,
                                )
                            }
                        }

                        isDocked -> {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                            )
                        }

                        else -> {
                            IconButton(onClick = onNavigationIconClick) {
                                Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                            }
                        }
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

                        expanded && uiState.searchQueryState.text.isNotEmpty() -> {
                            IconButton(
                                onClick = {
                                    uiState.searchQueryState.clearText()
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null,
                                )
                            }
                        }

                        expanded -> {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                            )
                        }

                        else -> {
                            IconButton(onClick = onNavigateToProfile) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = null,
                                )
                            }
                        }
                    }
                },
            )
        }

    TopSearchBar(
        state = searchBarState,
        inputField = inputField,
        modifier = modifier,
        scrollBehavior = scrollBehavior,
    )

    if (isDocked) {
        ExpandedDockedSearchBar(
            state = searchBarState,
            inputField = inputField,
            content = content,
        )
    } else {
        ExpandedFullScreenSearchBar(
            state = searchBarState,
            inputField = inputField,
            content = content,
        )
    }
}
