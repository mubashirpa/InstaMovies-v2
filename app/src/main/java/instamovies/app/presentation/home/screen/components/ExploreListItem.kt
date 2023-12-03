package instamovies.app.presentation.home.screen.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import instamovies.app.core.Constants
import instamovies.app.presentation.theme.InstaMoviesTheme

@Composable
fun ExploreListItem(
    posterPath: String?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(onClick = onClick, modifier = modifier) {
        AsyncImage(
            model =
                ImageRequest.Builder(LocalContext.current)
                    .data("${Constants.TMDB_BACKDROP_PREFIX}$posterPath")
                    .crossfade(true)
                    .build(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )
    }
}

@Preview
@Composable
fun ExploreListItemPreview() {
    InstaMoviesTheme {
        ExploreListItem(
            posterPath = null,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(4F / 5F),
            onClick = {},
        )
    }
}
