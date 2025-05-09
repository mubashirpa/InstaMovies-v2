package app.instamovies.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import app.instamovies.core.Constants
import app.instamovies.domain.model.movie.credits.MovieCast
import app.instamovies.domain.model.movie.credits.MovieCrew
import app.instamovies.domain.model.series.credits.SeriesCast
import app.instamovies.domain.model.series.credits.SeriesCrew
import app.instamovies.presentation.theme.InstaMoviesTheme

@Composable
fun CastListItem(
    cast: SeriesCast,
    onClick: (id: Int, name: String) -> Unit,
) {
    val id = cast.id
    val name = cast.name.orEmpty()

    CastListItemLayout(
        name = name,
        profilePath = cast.profilePath,
        character = cast.character,
        onClick = {
            if (id != null) {
                onClick(id, name)
            }
        },
    )
}

@Composable
fun CastListItem(
    crew: SeriesCrew,
    onClick: (id: Int, name: String) -> Unit,
) {
    val id = crew.id
    val name = crew.name.orEmpty()

    CastListItemLayout(
        name = name,
        profilePath = crew.profilePath,
        character = crew.job,
        onClick = {
            if (id != null) {
                onClick(id, name)
            }
        },
    )
}

@Composable
fun CastListItem(
    cast: MovieCast,
    onClick: (id: Int, name: String) -> Unit,
) {
    val id = cast.id
    val name = cast.name.orEmpty()

    CastListItemLayout(
        name = name,
        profilePath = cast.profilePath,
        character = cast.character,
        onClick = {
            if (id != null) {
                onClick(id, name)
            }
        },
    )
}

@Composable
fun CastListItem(
    crew: MovieCrew,
    onClick: (id: Int, name: String) -> Unit,
) {
    val id = crew.id
    val name = crew.name.orEmpty()

    CastListItemLayout(
        name = name,
        profilePath = crew.profilePath,
        character = crew.job,
        onClick = {
            if (id != null) {
                onClick(id, name)
            }
        },
    )
}

@Composable
private fun CastListItemLayout(
    name: String,
    profilePath: String?,
    modifier: Modifier = Modifier,
    character: String? = null,
    onClick: () -> Unit,
) {
    ListItem(
        headlineContent = {
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        },
        modifier = modifier.clickable(onClick = onClick),
        supportingContent =
            if (character != null) {
                {
                    Text(
                        text = character,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                }
            } else {
                null
            },
        leadingContent = {
            Card {
                AsyncImage(
                    model =
                        ImageRequest
                            .Builder(LocalContext.current)
                            .data("${Constants.TMDB_PROFILE_PREFIX}$profilePath")
                            .crossfade(true)
                            .build(),
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    contentScale = ContentScale.Crop,
                )
            }
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
    )
}

@Preview
@Composable
private fun CastListItemPreview() {
    InstaMoviesTheme(darkTheme = true) {
        CastListItemLayout(
            name = "Robert Downey Jr.",
            profilePath = null,
            character = "Tony Stark",
            onClick = {},
        )
    }
}
