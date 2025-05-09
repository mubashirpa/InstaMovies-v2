package app.instamovies.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import app.instamovies.domain.model.series.credits.SeriesCast
import app.instamovies.presentation.theme.InstaMoviesTheme

@Composable
fun CastGridItem(
    cast: MovieCast,
    onClick: (id: Int, name: String) -> Unit,
) {
    val id = cast.id
    val name = cast.name.orEmpty()

    CastGridItemLayout(
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
fun CastGridItem(
    cast: SeriesCast,
    onClick: (id: Int, name: String) -> Unit,
) {
    val id = cast.id
    val name = cast.name.orEmpty()

    CastGridItemLayout(
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
private fun CastGridItemLayout(
    name: String,
    profilePath: String?,
    modifier: Modifier = Modifier,
    character: String? = null,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier =
            modifier
                .width(80.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick,
                ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model =
                ImageRequest
                    .Builder(LocalContext.current)
                    .data("${Constants.TMDB_PROFILE_PREFIX}$profilePath")
                    .crossfade(true)
                    .build(),
            contentDescription = null,
            modifier =
                Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(CardDefaults.cardColors().containerColor)
                    .indication(interactionSource, ripple()),
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = name,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.labelMedium,
        )
        character?.also {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}

@Preview
@Composable
private fun CastGridItemPreview() {
    InstaMoviesTheme(darkTheme = true) {
        CastGridItemLayout(
            name = "Robert Downey Jr.",
            profilePath = null,
            character = "Tony Stark",
            onClick = {},
        )
    }
}
