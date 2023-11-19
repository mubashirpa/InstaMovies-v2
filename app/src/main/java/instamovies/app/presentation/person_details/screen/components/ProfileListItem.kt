package instamovies.app.presentation.person_details.screen.components

import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import instamovies.app.core.Constants

@Composable
fun ProfileListItem(
    filePath: String,
    modifier: Modifier = Modifier,
) {
    Card {
        AsyncImage(
            model =
                ImageRequest.Builder(LocalContext.current)
                    .data("${Constants.TMDB_STILL_PREFIX}$filePath")
                    .crossfade(true)
                    .build(),
            contentDescription = null,
            modifier = modifier,
            contentScale = ContentScale.Crop,
        )
    }
}
