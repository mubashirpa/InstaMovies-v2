package app.instamovies.presentation.person.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import app.instamovies.core.Constants
import app.instamovies.core.ext.scrim
import app.instamovies.core.util.enumValueOf
import app.instamovies.domain.model.MediaType
import app.instamovies.domain.model.person.popular.PersonResultModel
import app.instamovies.presentation.theme.InstaMoviesTheme

@Composable
fun PersonGridItem(
    result: PersonResultModel,
    onClick: (id: Int, name: String) -> Unit,
) {
    val id = result.id
    val name = result.name.orEmpty()
    val knownFor = result.knownFor?.firstOrNull()
    val knownForName =
        when (enumValueOf(knownFor?.mediaType?.uppercase()) as MediaType?) {
            MediaType.MOVIE -> knownFor?.title
            MediaType.TV -> knownFor?.name
            else -> null
        }

    PersonGridItemLayout(
        name = name,
        profilePath = result.profilePath,
        knownFor = knownForName,
        department = result.knownForDepartment,
        onClick = {
            if (id != null) {
                onClick(id, name)
            }
        },
    )
}

@Composable
private fun PersonGridItemLayout(
    name: String,
    profilePath: String?,
    modifier: Modifier = Modifier,
    knownFor: String? = null,
    department: String? = null,
    onClick: () -> Unit,
) {
    Card(onClick = onClick, modifier = modifier) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(4F / 5F),
        ) {
            AsyncImage(
                model =
                    ImageRequest
                        .Builder(LocalContext.current)
                        .data("${Constants.TMDB_PROFILE_PREFIX}$profilePath")
                        .crossfade(true)
                        .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds,
            )
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .scrim(
                            listOf(
                                Color.Transparent,
                                Color.Transparent,
                                MaterialTheme.colorScheme.surface,
                            ),
                        ),
            )
            Column(
                modifier =
                    Modifier
                        .padding(8.dp)
                        .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = name,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 18.sp),
                )
                if (knownFor != null || department != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text =
                            buildAnnotatedString {
                                if (!department.isNullOrEmpty()) {
                                    append("$department ")
                                }
                                if (!knownFor.isNullOrEmpty()) {
                                    append("($knownFor)")
                                }
                            },
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PersonGridItemPreview() {
    InstaMoviesTheme {
        PersonGridItemLayout(
            name = "Cillian Murphy",
            profilePath = null,
            modifier = Modifier.width(150.dp),
            knownFor = "Peaky Blinders",
            department = "Acting",
            onClick = {},
        )
    }
}
