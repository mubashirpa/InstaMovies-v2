package instamovies.app.presentation.home_container.screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import instamovies.app.core.util.enumValueOf
import instamovies.app.domain.model.MediaType
import instamovies.app.domain.model.search.SearchResultModel
import instamovies.app.domain.model.trending.TrendingResultModel
import instamovies.app.presentation.theme.InstaMoviesTheme
import instamovies.app.R.string as Strings

@Composable
fun SearchListItem(
    result: TrendingResultModel,
    onClick: (mediaType: MediaType, id: Int, name: String) -> Unit,
) {
    val id = result.id
    val mediaType: MediaType? = enumValueOf(result.mediaType?.uppercase())
    val title: String?
    val releaseYear: String?

    when (mediaType) {
        MediaType.MOVIE -> {
            title = result.title
            releaseYear = result.releaseDate?.take(4)
        }

        MediaType.TV -> {
            title = result.name
            releaseYear = result.firstAirDate?.take(4)
        }

        else -> {
            title = null
            releaseYear = null
        }
    }

    if (!title.isNullOrEmpty()) {
        SearchListItemLayout(
            title = title,
            imagePath = result.posterPath,
            mediaType = mediaType,
            releaseYear = releaseYear,
            onClick = {
                if (id != null && mediaType != null) {
                    onClick(mediaType, id, title)
                }
            },
        )
    }
}

@Composable
fun SearchListItem(
    result: SearchResultModel,
    onClick: (mediaType: MediaType, id: Int, name: String) -> Unit,
) {
    val id = result.id
    val mediaType: MediaType? = enumValueOf(result.mediaType?.uppercase())
    val title: String?
    val imagePath: String?
    val releaseYear: String?

    when (mediaType) {
        MediaType.MOVIE -> {
            title = result.title
            imagePath = result.posterPath
            releaseYear = result.releaseDate?.take(4)
        }

        MediaType.PERSON -> {
            title = result.name
            imagePath = result.profilePath
            releaseYear = null
        }

        MediaType.TV -> {
            title = result.name
            imagePath = result.posterPath
            releaseYear = result.firstAirDate?.take(4)
        }

        else -> {
            title = null
            imagePath = null
            releaseYear = null
        }
    }

    if (!title.isNullOrEmpty()) {
        SearchListItemLayout(
            title = title,
            imagePath = imagePath,
            mediaType = mediaType,
            releaseYear = releaseYear,
            onClick = {
                if (id != null && mediaType != null) {
                    onClick(mediaType, id, title)
                }
            },
        )
    }
}

@Composable
private fun SearchListItemLayout(
    title: String,
    imagePath: String?,
    mediaType: MediaType?,
    releaseYear: String?,
    onClick: () -> Unit,
) {
    ListItem(
        headlineContent = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
            )
        },
        modifier = Modifier.clickable(onClick = onClick),
        overlineContent = {
            val text =
                if (mediaType == MediaType.TV) {
                    stringResource(id = Strings.series).uppercase()
                } else {
                    mediaType?.name.orEmpty()
                }
            Text(text = text.uppercase())
        },
        leadingContent = {
            Card {
                if (imagePath != null) {
                    val imageUri =
                        if (mediaType == MediaType.PERSON) {
                            "${Constants.TMDB_PROFILE_PREFIX}$imagePath"
                        } else {
                            "${Constants.TMDB_POSTER_PREFIX}$imagePath"
                        }
                    AsyncImage(
                        model =
                            ImageRequest.Builder(LocalContext.current)
                                .data(imageUri)
                                .crossfade(true)
                                .build(),
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Box(modifier = Modifier.size(56.dp))
                }
            }
        },
        trailingContent =
            if (releaseYear.isNullOrEmpty()) {
                null
            } else {
                {
                    Text(text = releaseYear)
                }
            },
    )
}

@Preview
@Composable
private fun SearchListItemPreview() {
    InstaMoviesTheme {
        Column {
            SearchListItem(
                result =
                    TrendingResultModel(
                        title = "Transformers: Rise of the Beasts",
                        mediaType = "movie",
                        releaseDate = "2023",
                    ),
                onClick = { _, _, _ -> },
            )
            Spacer(modifier = Modifier.size(10.dp))
            SearchListItem(
                result =
                    SearchResultModel(
                        name = "Leonardo DiCaprio",
                        mediaType = "person",
                    ),
                onClick = { _, _, _ -> },
            )
        }
    }
}
