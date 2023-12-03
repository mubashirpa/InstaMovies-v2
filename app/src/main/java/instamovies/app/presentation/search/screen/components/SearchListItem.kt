package instamovies.app.presentation.search.screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import instamovies.app.core.Constants
import instamovies.app.core.ext.roundHighest
import instamovies.app.core.util.enumValueOf
import instamovies.app.data.remote.dto.search.KnownFor
import instamovies.app.domain.model.MediaType
import instamovies.app.domain.model.search.SearchResultModel
import instamovies.app.presentation.components.RatingBar
import instamovies.app.presentation.theme.InstaMoviesTheme
import instamovies.app.R.string as Strings

@Composable
fun SearchListItem(
    result: SearchResultModel,
    onClick: (mediaType: MediaType, id: Int, name: String) -> Unit,
) {
    val id = result.id
    val mediaType: MediaType? = enumValueOf(result.mediaType?.uppercase())
    val title: String?
    val imagePath: String?
    val mediaTypeName: String?
    val releaseYear: String?

    when (mediaType) {
        MediaType.MOVIE -> {
            title = result.title
            imagePath = result.posterPath
            mediaTypeName = mediaType.name
            releaseYear = result.releaseDate?.take(4)

            if (!title.isNullOrEmpty()) {
                MovieListItem(
                    title = title,
                    posterPath = imagePath,
                    mediaTypeName = mediaTypeName,
                    genres = convertIdsToGenres(result.genreIds, mediaType),
                    releaseYear = releaseYear,
                    rating = result.voteAverage?.roundHighest(),
                    onClick = {
                        if (id != null) {
                            onClick(mediaType, id, "")
                        }
                    },
                )
            }
        }

        MediaType.PERSON -> {
            title = result.name
            imagePath = result.profilePath
            val knownFor =
                result.knownFor?.joinToString(separator = ", ") {
                    val castTitle: String =
                        when (enumValueOf(it.mediaType?.uppercase()) as MediaType?) {
                            MediaType.MOVIE -> {
                                it.title.orEmpty()
                            }

                            MediaType.TV -> {
                                it.name.orEmpty()
                            }

                            else -> {
                                ""
                            }
                        }
                    val castReleaseYear = it.releaseDate?.take(4)
                    castTitle.plus(if (castReleaseYear.isNullOrBlank()) "" else " ($castReleaseYear)")
                }

            if (!title.isNullOrEmpty()) {
                PersonListItem(
                    name = title,
                    profilePath = imagePath,
                    department = result.knownForDepartment,
                    knownFor = knownFor,
                    onClick = {
                        if (id != null) {
                            onClick(mediaType, id, title)
                        }
                    },
                )
            }
        }

        MediaType.TV -> {
            title = result.name
            imagePath = result.posterPath
            mediaTypeName = stringResource(id = Strings.series).uppercase()
            releaseYear = result.firstAirDate?.take(4)

            if (!title.isNullOrEmpty()) {
                MovieListItem(
                    title = title,
                    posterPath = imagePath,
                    mediaTypeName = mediaTypeName,
                    genres = convertIdsToGenres(result.genreIds, mediaType),
                    releaseYear = releaseYear,
                    rating = result.voteAverage?.roundHighest(),
                    onClick = {
                        if (id != null) {
                            onClick(mediaType, id, "")
                        }
                    },
                )
            }
        }

        else -> {}
    }
}

@Composable
private fun MovieListItem(
    title: String,
    posterPath: String?,
    mediaTypeName: String?,
    genres: String?,
    releaseYear: String?,
    rating: Double?,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
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
            overlineContent =
                if (mediaTypeName != null) {
                    {
                        Text(text = mediaTypeName)
                    }
                } else {
                    null
                },
            supportingContent = {
                Column {
                    genres?.let {
                        Text(text = it, overflow = TextOverflow.Ellipsis, maxLines = 1)
                    }
                    rating?.let {
                        if (rating > 0) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RatingBar(rating = it / 2, starSize = 16.dp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "$it", fontSize = 16.sp)
                            }
                        }
                    }
                }
            },
            leadingContent = {
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
                                .width(80.dp)
                                .aspectRatio(2F / 3F),
                        contentScale = ContentScale.FillBounds,
                    )
                }
            },
            trailingContent =
                if (releaseYear != null) {
                    {
                        Text(text = releaseYear)
                    }
                } else {
                    null
                },
        )
    }
}

@Composable
private fun PersonListItem(
    name: String,
    profilePath: String?,
    department: String? = null,
    knownFor: String? = null,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )
            },
            overlineContent =
                if (department != null) {
                    {
                        Text(text = department.uppercase())
                    }
                } else {
                    null
                },
            supportingContent =
                if (knownFor != null) {
                    {
                        Text(text = knownFor, overflow = TextOverflow.Ellipsis, maxLines = 2)
                    }
                } else {
                    null
                },
            leadingContent = {
                Card {
                    if (profilePath == null) {
                        Box(
                            modifier =
                                Modifier
                                    .width(80.dp)
                                    .aspectRatio(2F / 3F),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                            )
                        }
                    } else {
                        SubcomposeAsyncImage(
                            model = "${Constants.TMDB_PROFILE_PREFIX}$profilePath",
                            contentDescription = null,
                            modifier =
                                Modifier
                                    .width(80.dp)
                                    .aspectRatio(2F / 3F),
                            contentScale = ContentScale.FillBounds,
                        ) {
                            val state = painter.state
                            if (state is AsyncImagePainter.State.Error) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        modifier = Modifier.size(40.dp),
                                    )
                                }
                            } else {
                                SubcomposeAsyncImageContent()
                            }
                        }
                    }
                }
            },
        )
    }
}

private fun convertIdsToGenres(
    genreIds: List<Int>?,
    mediaType: MediaType?,
): String? {
    if (genreIds.isNullOrEmpty() || mediaType == null) {
        return null
    }
    val genres: Map<Int, String> =
        when (mediaType) {
            MediaType.MOVIE -> {
                mapOf(
                    28 to "Action",
                    12 to "Adventure",
                    16 to "Animation",
                    25 to "Comedy",
                    80 to "Crime",
                    99 to "Documentary",
                    18 to "Drama",
                    10751 to "Family",
                    14 to "Fantasy",
                    36 to "History",
                    27 to "Horror",
                    10402 to "Music",
                    9648 to "Mystery",
                    10749 to "Romance",
                    878 to "Science Fiction",
                    10770 to "TV Movie",
                    53 to "Thriller",
                    10752 to "War",
                    37 to "Western",
                )
            }

            MediaType.TV -> {
                mapOf(
                    10759 to "Action & Adventure",
                    16 to "Animation",
                    35 to "Comedy",
                    80 to "Crime",
                    99 to "Documentary",
                    18 to "Drama",
                    10751 to "Family",
                    10762 to "Kids",
                    9648 to "Mystery",
                    10763 to "News",
                    10764 to "Reality",
                    10765 to "Sci-Fi & Fantasy",
                    10766 to "Soap",
                    10767 to "Talk",
                    10768 to "War & Politics",
                    37 to "Western",
                )
            }

            else -> {
                emptyMap()
            }
        }
    return genres.filterKeys { it in genreIds }.values.joinToString(", ").ifBlank { null }
}

@Preview
@Composable
private fun SearchListItemPreview() {
    InstaMoviesTheme {
        Column {
            SearchListItem(
                result =
                    SearchResultModel(
                        title = "Transformers: Rise of the Beasts",
                        mediaType = "movie",
                        releaseDate = "2023",
                        genreIds = listOf(28, 12, 878),
                        voteAverage = 9.6,
                    ),
                onClick = { _, _, _ -> },
            )
            Spacer(modifier = Modifier.size(10.dp))
            SearchListItem(
                result =
                    SearchResultModel(
                        name = "Leonardo DiCaprio",
                        knownForDepartment = "Acting",
                        knownFor =
                            listOf(
                                KnownFor(
                                    mediaType = "movie",
                                    title = "Inception",
                                    releaseDate = "2010",
                                ),
                                KnownFor(
                                    mediaType = "movie",
                                    title = "Titanic",
                                    releaseDate = "1997",
                                ),
                                KnownFor(
                                    mediaType = "movie",
                                    title = "Shutter Island",
                                    releaseDate = "2010",
                                ),
                            ),
                        mediaType = "person",
                    ),
                onClick = { _, _, _ -> },
            )
        }
    }
}
