package instamovies.app.presentation.tv_show_details.screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import instamovies.app.core.Constants
import instamovies.app.core.ext.roundHighest
import instamovies.app.core.util.DateUtils
import instamovies.app.domain.model.series.details.SeriesSeason
import instamovies.app.presentation.theme.InstaMoviesTheme
import instamovies.app.R.string as Strings

@Composable
fun SeasonListItem(
    seriesName: String,
    season: SeriesSeason,
    onClick: (id: Int) -> Unit,
) {
    SeasonListItemLayout(
        seasonName = season.name.orEmpty(),
        posterPath = season.posterPath,
        airDate = season.airDate,
        episodeCount = season.episodeCount,
        overview = season.overview,
        rating = season.voteAverage?.roundHighest(),
        seasonNumber = season.seasonNumber ?: 0,
        seriesName = seriesName,
        onClick = {
            season.id?.also { id ->
                onClick(id)
            }
        },
    )
}

@Composable
private fun SeasonListItemLayout(
    seasonName: String,
    posterPath: String?,
    modifier: Modifier = Modifier,
    airDate: String? = null,
    episodeCount: Int? = null,
    overview: String? = null,
    rating: Double? = null,
    seasonNumber: Int,
    seriesName: String,
    onClick: () -> Unit,
) {
    val hasRating = rating != null && rating > 0
    val interactionSource = remember { MutableInteractionSource() }
    val year = DateUtils.formatStringDateTime(airDate.orEmpty(), "yyyy-MM-dd", "yyyy")
    val premierDate =
        DateUtils.formatStringDateTime(airDate.orEmpty(), "yyyy-MM-dd", "dd LLLL yyyy")
    val hasYear = !year.isNullOrEmpty()

    ListItem(
        headlineContent = {
            Text(
                text = seasonName,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
            )
        },
        modifier =
            modifier.clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
        overlineContent =
            if (episodeCount != null) {
                {
                    Text(text = "$episodeCount Episodes")
                }
            } else {
                null
            },
        supportingContent = {
            Column(
                modifier = Modifier.padding(top = 4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                if (hasRating || hasYear) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (hasRating) {
                            Surface(
                                shape = MaterialTheme.shapes.small,
                                color = AssistChipDefaults.elevatedAssistChipColors().containerColor,
                                tonalElevation = 1.dp,
                                shadowElevation = 1.dp,
                            ) {
                                Row(
                                    modifier =
                                        Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp,
                                        ),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "$rating",
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.labelSmall,
                                    )
                                }
                            }
                        }
                        if (hasYear) {
                            Text(text = year!!)
                        }
                    }
                }
                if (overview.isNullOrEmpty()) {
                    Text(
                        text =
                            stringResource(
                                id = Strings.season_overview_placeholder,
                                seasonNumber,
                                seriesName,
                                premierDate.orEmpty(),
                            ),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 3,
                    )
                } else {
                    Text(text = overview, overflow = TextOverflow.Ellipsis, maxLines = 3)
                }
            }
        },
        leadingContent = {
            Card(
                modifier =
                    Modifier
                        .width(80.dp)
                        .aspectRatio(2F / 3F),
            ) {
                AsyncImage(
                    model =
                        ImageRequest
                            .Builder(LocalContext.current)
                            .data("${Constants.TMDB_POSTER_PREFIX}$posterPath")
                            .crossfade(true)
                            .build(),
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .indication(interactionSource, ripple()),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                )
            }
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
    )
}

@Preview
@Composable
private fun SearchListItemPreview() {
    InstaMoviesTheme(darkTheme = true) {
        SeasonListItemLayout(
            seasonName = "Season 1",
            posterPath = null,
            airDate = "2008-01-20",
            episodeCount = 7,
            overview = "High school chemistry teacher Walter White's life is suddenly transformed by a dire medical diagnosis.",
            rating = 8.2,
            seasonNumber = 1,
            seriesName = "Breaking Bad",
            onClick = {},
        )
    }
}
