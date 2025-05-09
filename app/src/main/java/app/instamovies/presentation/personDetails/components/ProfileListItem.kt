package app.instamovies.presentation.personDetails.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import app.instamovies.core.Constants

@Composable
fun ProfileListItem(
    filePath: String?,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model =
            ImageRequest
                .Builder(LocalContext.current)
                .data("${Constants.TMDB_STILL_PREFIX}$filePath")
                .crossfade(true)
                .build(),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop,
    )
}
