package app.instamovies.presentation.homeContainer.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import app.instamovies.core.Constants
import app.instamovies.core.util.enumValueOf
import app.instamovies.domain.model.MediaType
import app.instamovies.domain.model.search.SearchResultModel
import app.instamovies.domain.model.trending.TrendingResultModel
import app.instamovies.presentation.theme.InstaMoviesTheme
import app.instamovies.R.string as Strings

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

    title?.let {
        SearchListItemLayout(
            title = it,
            imagePath = result.posterPath,
            mediaType = mediaType,
            releaseYear = releaseYear,
            onClick = {
                id?.let { safeId ->
                    mediaType?.let { safeMediaType ->
                        onClick(safeMediaType, safeId, title)
                    }
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

    title?.let {
        SearchListItemLayout(
            title = it,
            imagePath = imagePath,
            mediaType = mediaType,
            releaseYear = releaseYear,
            onClick = {
                id?.let { safeId ->
                    mediaType?.let { safeMediaType ->
                        onClick(safeMediaType, safeId, title)
                    }
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
                            ImageRequest
                                .Builder(LocalContext.current)
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
            releaseYear?.let {
                {
                    Text(text = releaseYear)
                }
            },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
    )
}

@Preview(showBackground = true)
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
