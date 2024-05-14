package instamovies.app.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import instamovies.app.core.Constants
import instamovies.app.core.ext.roundHighest
import instamovies.app.core.util.enumValueOf
import instamovies.app.domain.model.MediaType
import instamovies.app.domain.model.movie.MovieResultModel
import instamovies.app.domain.model.person.credits.PersonCast
import instamovies.app.domain.model.series.SeriesResultModel
import instamovies.app.domain.model.trending.TrendingResultModel
import instamovies.app.presentation.theme.InstaMoviesTheme
import instamovies.app.R.string as Strings

@Composable
fun MediaGridItem(
    result: MovieResultModel,
    onClick: (id: Int) -> Unit,
) {
    val id = result.id

    MediaGridItemLayout(
        title = result.title.orEmpty(),
        posterPath = result.posterPath,
        mediaTypeName = MediaType.MOVIE.name,
        releaseYear = result.releaseDate?.take(4),
        rating = result.voteAverage,
        onClick = {
            if (id != null) {
                onClick(id)
            }
        },
    )
}

@Composable
fun MediaGridItem(
    cast: PersonCast,
    onClick: (mediaType: MediaType, id: Int) -> Unit,
) {
    val id = cast.id
    val mediaType: MediaType? = enumValueOf(cast.mediaType?.uppercase())
    val title: String?
    val mediaTypeName: String?
    val releaseYear: String?

    when (mediaType) {
        MediaType.MOVIE -> {
            title = cast.title
            mediaTypeName = mediaType.name
            releaseYear = cast.releaseDate?.take(4)
        }

        MediaType.TV -> {
            title = cast.name
            mediaTypeName = stringResource(id = Strings.series).uppercase()
            releaseYear = cast.firstAirDate?.take(4)
        }

        else -> {
            title = null
            mediaTypeName = null
            releaseYear = null
        }
    }

    MediaGridItemLayout(
        title = title.orEmpty(),
        posterPath = cast.posterPath,
        mediaTypeName = mediaTypeName,
        releaseYear = releaseYear,
        rating = cast.voteAverage,
        onClick = {
            if (mediaType != null && id != null) {
                onClick(mediaType, id)
            }
        },
    )
}

@Composable
fun MediaGridItem(
    result: SeriesResultModel,
    onClick: (id: Int) -> Unit,
) {
    val id = result.id

    MediaGridItemLayout(
        title = result.name.orEmpty(),
        posterPath = result.posterPath,
        mediaTypeName = stringResource(id = Strings.series).uppercase(),
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
fun MediaGridItem(
    result: TrendingResultModel,
    onClick: (mediaType: MediaType, id: Int) -> Unit,
) {
    val id = result.id
    val mediaType: MediaType? = enumValueOf(result.mediaType?.uppercase())
    val title: String?
    val mediaTypeName: String?
    val releaseYear: String?

    when (mediaType) {
        MediaType.MOVIE -> {
            title = result.title
            mediaTypeName = mediaType.name
            releaseYear = result.releaseDate?.take(4)
        }

        MediaType.TV -> {
            title = result.name
            mediaTypeName = stringResource(id = Strings.series).uppercase()
            releaseYear = result.firstAirDate?.take(4)
        }

        else -> {
            title = null
            mediaTypeName = null
            releaseYear = null
        }
    }

    MediaGridItemLayout(
        title = title.orEmpty(),
        posterPath = result.posterPath,
        mediaTypeName = mediaTypeName,
        releaseYear = releaseYear,
        rating = result.voteAverage,
        onClick = {
            if (mediaType != null && id != null) {
                onClick(mediaType, id)
            }
        },
    )
}

@Composable
private fun MediaGridItemLayout(
    title: String,
    posterPath: String?,
    modifier: Modifier = Modifier,
    mediaTypeName: String? = null,
    releaseYear: String? = null,
    rating: Double? = null,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier =
            modifier
                .width(100.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick,
                ),
    ) {
        Box {
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
            mediaTypeName?.let {
                Surface(
                    modifier =
                        Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                    shape = MaterialTheme.shapes.small,
                    color = AssistChipDefaults.elevatedAssistChipColors().containerColor,
                    tonalElevation = 1.dp,
                    shadowElevation = 1.dp,
                ) {
                    Text(
                        text = mediaTypeName,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        }
        Text(
            text = title,
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
private fun MediaGridItemPreview() {
    InstaMoviesTheme(darkTheme = true) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MediaGridItemLayout(
                title = "Interstellar",
                posterPath = null,
                mediaTypeName = "MOVIE",
                releaseYear = "2014",
                rating = 8.4,
                onClick = {},
            )
            MediaGridItemLayout(
                title = "Breaking Bad",
                posterPath = null,
                mediaTypeName = "SERIES",
                releaseYear = "2008",
                rating = 8.9,
                onClick = {},
            )
        }
    }
}
