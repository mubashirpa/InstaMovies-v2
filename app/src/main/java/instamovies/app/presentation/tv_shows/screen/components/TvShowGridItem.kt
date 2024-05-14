package instamovies.app.presentation.tv_shows.screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import instamovies.app.core.Constants
import instamovies.app.core.ext.roundHighest
import instamovies.app.domain.model.series.SeriesResultModel
import instamovies.app.presentation.theme.InstaMoviesTheme

@Composable
fun TvShowGridItem(
    result: SeriesResultModel,
    onClick: (id: Int) -> Unit,
) {
    val id = result.id

    TvShowGridItemLayout(
        name = result.name.orEmpty(),
        posterPath = result.posterPath,
        releaseYear = result.firstAirDate?.take(4),
        rating = result.voteAverage,
        onClick = {
            if (id != null) {
                onClick(id)
            }
        },
    )
}

@Composable
private fun TvShowGridItemLayout(
    name: String,
    posterPath: String?,
    modifier: Modifier = Modifier,
    releaseYear: String? = null,
    rating: Double? = null,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier =
            modifier.clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
    ) {
        Card {
            AsyncImage(
                model =
                    ImageRequest.Builder(LocalContext.current)
                        .data("${Constants.TMDB_POSTER_PREFIX}$posterPath")
                        .crossfade(true)
                        .build(),
                contentDescription = null,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(4F / 5F)
                        .indication(interactionSource, ripple()),
                contentScale = ContentScale.FillBounds,
            )
        }
        Text(
            text = name,
            modifier = Modifier.padding(top = 4.dp),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 18.sp),
        )
        Row(
            modifier = Modifier.padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            releaseYear?.let { year ->
                Text(
                    text = year,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            rating?.let {
                if (it > 0) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = " ${it.roundHighest()}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MovieGridItemPreview() {
    InstaMoviesTheme(darkTheme = true) {
        TvShowGridItemLayout(
            name = "Breaking Bad",
            posterPath = null,
            modifier = Modifier.width(100.dp),
            releaseYear = "2008",
            rating = 8.9,
            onClick = {},
        )
    }
}
