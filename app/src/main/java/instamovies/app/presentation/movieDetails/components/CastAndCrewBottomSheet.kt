package instamovies.app.presentation.movieDetails.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import instamovies.app.domain.model.movie.credits.MovieCredits
import instamovies.app.presentation.components.CastListItem
import kotlinx.coroutines.launch
import instamovies.app.R.string as Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CastAndCrewBottomSheet(
    isOpen: Boolean,
    credits: MovieCredits?,
    innerPadding: PaddingValues,
    navigateToPersonDetails: (id: Int, name: String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    if (isOpen) {
        val scrollState = rememberLazyListState()
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val scope = rememberCoroutineScope()
        val isFullscreen =
            (sheetState.targetValue == SheetValue.Expanded) &&
                (scrollState.canScrollForward || scrollState.canScrollBackward)
        val cornerSize by animateDpAsState(
            targetValue = if (isFullscreen) 0.dp else 28.dp,
            label = stringResource(id = Strings.label_animate_bottom_sheet_corner_size),
        )

        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = cornerSize, topEnd = cornerSize),
            dragHandle = {
                val topPadding by animateDpAsState(
                    targetValue = if (isFullscreen) innerPadding.calculateTopPadding() else 0.dp,
                    label = stringResource(id = Strings.label_animate_bottom_sheet_top_padding),
                )
                BottomSheetDefaults.DragHandle(modifier = Modifier.padding(top = topPadding))
            },
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                state = scrollState,
                content = {
                    val casts = credits?.cast.orEmpty()
                    item {
                        Text(
                            text = "Cast ${casts.size}",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                    items(casts) { cast ->
                        CastListItem(
                            cast = cast,
                            onClick = { id, name ->
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        onDismissRequest()
                                        navigateToPersonDetails(id, name)
                                    }
                                }
                            },
                        )
                    }

                    val crews = credits?.crew.orEmpty()
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Crew ${crews.size}",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                    items(crews) { crew ->
                        CastListItem(
                            crew = crew,
                            onClick = { id, name ->
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        onDismissRequest()
                                        navigateToPersonDetails(id, name)
                                    }
                                }
                            },
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(innerPadding.calculateBottomPadding() + 8.dp))
                    }
                },
            )
        }
    }
}
