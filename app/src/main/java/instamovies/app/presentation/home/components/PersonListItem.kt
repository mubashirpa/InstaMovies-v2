package instamovies.app.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import instamovies.app.core.Constants
import instamovies.app.domain.model.trending.TrendingPersonResultModel
import instamovies.app.presentation.theme.InstaMoviesTheme

@Composable
fun PersonListItem(
    result: TrendingPersonResultModel,
    onClick: (id: Int, name: String) -> Unit,
) {
    val id = result.id
    val name = result.name.orEmpty()

    PersonListItemLayout(
        name = name,
        profilePath = result.profilePath,
        department = result.knownForDepartment,
        onClick = {
            if (id != null) {
                onClick(id, name)
            }
        },
    )
}

@Composable
private fun PersonListItemLayout(
    name: String,
    profilePath: String?,
    modifier: Modifier = Modifier,
    department: String? = null,
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
        Card {
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
                        .fillMaxWidth()
                        .aspectRatio(1F)
                        .indication(interactionSource, ripple()),
                contentScale = ContentScale.Crop,
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
        department?.also {
            Text(
                text = it,
                modifier = Modifier.padding(top = 4.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview
@Composable
private fun PersonListItemPreview() {
    InstaMoviesTheme(darkTheme = true) {
        PersonListItemLayout(
            name = "Robert Downey Jr.",
            profilePath = null,
            department = "Acting",
            onClick = {},
        )
    }
}
