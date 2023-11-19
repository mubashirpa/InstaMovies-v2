package instamovies.app.presentation.movie_details.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.window.layout.DisplayFeature
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import instamovies.app.core.Constants
import instamovies.app.core.ext.roundHighest
import instamovies.app.core.ext.scrim
import instamovies.app.core.util.InstaMoviesContentType
import instamovies.app.core.util.Resource
import instamovies.app.domain.model.movie.MovieResultModel
import instamovies.app.domain.model.movie.credits.MovieCast
import instamovies.app.domain.model.movie.details.MovieDetails
import instamovies.app.domain.model.movie.details.MovieGenre
import instamovies.app.presentation.components.BackButton
import instamovies.app.presentation.components.CastGridItem
import instamovies.app.presentation.components.ErrorScreen
import instamovies.app.presentation.components.LoadingIndicator
import instamovies.app.presentation.components.MediaGridItem
import instamovies.app.presentation.movie_details.MovieDetailsUiEvent
import instamovies.app.presentation.movie_details.MovieDetailsUiState
import instamovies.app.presentation.movie_details.screen.components.CastAndCrewBottomSheet
import instamovies.app.presentation.theme.InstaMoviesDynamicTheme
import instamovies.app.R.string as Strings

@Composable
fun MovieDetailsScreen(
    contentType: InstaMoviesContentType,
    displayFeatures: List<DisplayFeature>,
    uiState: MovieDetailsUiState,
    onEvent: (MovieDetailsUiEvent) -> Unit,
    navigateToMovieDetails: (id: Int) -> Unit,
    navigateToPersonDetails: (id: Int, name: String) -> Unit,
    onBackPressed: () -> Unit,
) {
    val movieDetailsResource = uiState.movieDetailsResource

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        when (movieDetailsResource) {
            is Resource.Empty -> {}

            is Resource.Error -> {
                ErrorScreen(
                    onRetry = {
                        onEvent(MovieDetailsUiEvent.OnRetry)
                    },
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                    message = movieDetailsResource.message!!.asString(),
                )
            }

            is Resource.Loading -> {
                LoadingIndicator(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                )
            }

            is Resource.Success -> {
                val details = movieDetailsResource.data
                if (details != null) {
                    InstaMoviesDynamicTheme(imageUrl = "${Constants.TMDB_BACKDROP_PREFIX}${details.backdropPath}") {
                        if (contentType == InstaMoviesContentType.DUAL_PANE) {
                            TwoPane(
                                first = {
                                    Header(
                                        details = details,
                                        modifier = Modifier.fillMaxSize(),
                                        isDualPane = true,
                                        windowInsets = WindowInsets(0),
                                        onBackPressed = onBackPressed,
                                    )
                                },
                                second = {
                                    LazyColumn(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        content = {
                                            val overview = details.overview
                                            if (!overview.isNullOrEmpty()) {
                                                item {
                                                    Overview(
                                                        overview = overview,
                                                        genres = details.genres.orEmpty(),
                                                    )
                                                }
                                            }

                                            val casts = details.credits?.cast.orEmpty()
                                            if (casts.isNotEmpty()) {
                                                item {
                                                    TopBilledCast(
                                                        casts = casts,
                                                        onViewAllClick = {
                                                            onEvent(
                                                                MovieDetailsUiEvent.OnCastAndCrewBottomSheetOpenChange(
                                                                    true,
                                                                ),
                                                            )
                                                        },
                                                        navigateToPersonDetails = navigateToPersonDetails,
                                                    )
                                                }
                                            }

                                            val recommendations = details.recommendations.orEmpty()
                                            if (recommendations.isNotEmpty()) {
                                                item {
                                                    Recommendations(
                                                        recommendations = recommendations,
                                                        navigateToMovieDetails = navigateToMovieDetails,
                                                    )
                                                }
                                            }

                                            item { Spacer(modifier = Modifier.height(8.dp)) }
                                        },
                                    )
                                },
                                strategy =
                                    HorizontalTwoPaneStrategy(
                                        splitFraction = 0.5F,
                                        gapWidth = 16.dp,
                                    ),
                                displayFeatures = displayFeatures,
                                modifier = Modifier.padding(innerPadding),
                            )
                        } else {
                            MovieDetailsSinglePainContent(
                                onEvent = onEvent,
                                innerPadding = innerPadding,
                                details = details,
                                navigateToMovieDetails = navigateToMovieDetails,
                                navigateToPersonDetails = navigateToPersonDetails,
                                onBackPressed = onBackPressed,
                            )
                        }

                        CastAndCrewBottomSheet(
                            isOpen = uiState.openCastAndCrewBottomSheet,
                            credits = details.credits,
                            innerPadding = innerPadding,
                            navigateToPersonDetails = navigateToPersonDetails,
                            onDismissRequest = {
                                onEvent(
                                    MovieDetailsUiEvent.OnCastAndCrewBottomSheetOpenChange(
                                        false,
                                    ),
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MovieDetailsSinglePainContent(
    onEvent: (MovieDetailsUiEvent) -> Unit,
    innerPadding: PaddingValues,
    details: MovieDetails,
    navigateToMovieDetails: (id: Int) -> Unit,
    navigateToPersonDetails: (id: Int, name: String) -> Unit,
    onBackPressed: () -> Unit,
) {
    val layoutDirection = LocalLayoutDirection.current
    val contentPadding =
        PaddingValues(
            start = innerPadding.calculateStartPadding(layoutDirection),
            end = innerPadding.calculateEndPadding(layoutDirection),
            bottom = innerPadding.calculateBottomPadding(),
        )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = {
            item {
                Header(
                    details = details,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(1F),
                    isDualPane = false,
                    onBackPressed = onBackPressed,
                )
            }

            val overview = details.overview
            if (!overview.isNullOrEmpty()) {
                item {
                    Overview(
                        overview = overview,
                        genres = details.genres.orEmpty(),
                    )
                }
            }

            val casts = details.credits?.cast.orEmpty()
            if (casts.isNotEmpty()) {
                item {
                    TopBilledCast(
                        casts = casts,
                        onViewAllClick = {
                            onEvent(MovieDetailsUiEvent.OnCastAndCrewBottomSheetOpenChange(true))
                        },
                        navigateToPersonDetails = navigateToPersonDetails,
                    )
                }
            }

            val recommendations = details.recommendations.orEmpty()
            if (recommendations.isNotEmpty()) {
                item {
                    Recommendations(
                        recommendations = recommendations,
                        navigateToMovieDetails = navigateToMovieDetails,
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        },
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun Header(
    details: MovieDetails,
    modifier: Modifier = Modifier,
    isDualPane: Boolean,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    onBackPressed: () -> Unit,
) {
    val releaseYear = details.releaseDate?.take(4)
    val runtime =
        details.runtime?.let {
            if (it > 0) {
                val hour = it / 60
                val minute = it % 60
                stringResource(id = Strings._hour_minute, hour, minute)
            } else {
                ""
            }
        }
    // TODO("Fix this")
    val contentRating = "PG-13"
    val rating = details.voteAverage?.roundHighest()?.toString()
    var cardModifier = modifier
    var cardShape = RectangleShape
    var scrim =
        listOf(
            Color.Transparent,
            Color.Transparent,
            MaterialTheme.colorScheme.surface,
        )
    var contentColor = MaterialTheme.colorScheme.onSurface

    if (isDualPane) {
        cardModifier =
            modifier.then(
                Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp,
                ),
            )
        cardShape = CardDefaults.shape
        scrim =
            listOf(
                Color.Transparent,
                Color.Transparent,
                Color.Black,
            )
        contentColor = Color.White
    }

    Card(
        modifier = cardModifier,
        shape = cardShape,
    ) {
        Box {
            AsyncImage(
                model =
                    ImageRequest.Builder(LocalContext.current)
                        .data("${Constants.TMDB_BACKDROP_PREFIX}${details.backdropPath}")
                        .crossfade(true)
                        .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                alignment = Alignment.TopCenter,
                contentScale = ContentScale.Crop,
            )
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .scrim(scrim),
            )
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = details.title.orEmpty(),
                    modifier = Modifier.basicMarquee(),
                    color = contentColor,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    style = MaterialTheme.typography.headlineSmall,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SubtitleItem(
                        icon = Icons.Default.CalendarToday,
                        value = releaseYear,
                        contentColor = contentColor,
                    )
                    SubtitleItem(
                        icon = Icons.Default.Timer,
                        value = runtime,
                        contentColor = contentColor,
                    )
                    SubtitleItem(
                        icon = Icons.Default.Info,
                        value = contentRating,
                        contentColor = contentColor,
                    )
                    SubtitleItem(
                        icon = Icons.Default.Star,
                        value = rating,
                        contentColor = contentColor,
                    )
                }
            }
            TopAppBar(
                title = {},
                navigationIcon = {
                    BackButton(
                        onClick = onBackPressed,
                        colors =
                            IconButtonDefaults.iconButtonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black,
                            ),
                    )
                },
                windowInsets = windowInsets,
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
            )
        }
    }
}

@Composable
private fun SubtitleItem(
    icon: ImageVector,
    value: String?,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    if (!value.isNullOrEmpty()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = contentColor,
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = value,
                color = contentColor,
                maxLines = 1,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Overview(
    overview: String,
    genres: List<MovieGenre>,
) {
    Text(
        text = stringResource(id = Strings.overview),
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.titleMedium,
    )
    Text(
        text = overview,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = 16.dp),
        style = MaterialTheme.typography.bodyMedium,
    )
    if (genres.isNotEmpty()) {
        FlowRow(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            genres.forEach { genre ->
                ElevatedAssistChip(
                    onClick = { /*TODO*/ },
                    label = {
                        Text(text = genre.name.orEmpty())
                    },
                )
            }
        }
    }
}

@Composable
private fun TopBilledCast(
    casts: List<MovieCast>,
    navigateToPersonDetails: (id: Int, name: String) -> Unit,
    onViewAllClick: () -> Unit,
) {
    Text(
        text = stringResource(id = Strings.label_top_billed_cast),
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.titleMedium,
    )
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        content = {
            items(casts) { cast ->
                CastGridItem(
                    cast = cast,
                    onClick = navigateToPersonDetails,
                )
            }
        },
    )
    Spacer(modifier = Modifier.height(8.dp))
    AssistChip(
        onClick = onViewAllClick,
        label = {
            Text(text = stringResource(id = Strings.label_full_cast_and_crew))
        },
        modifier = Modifier.padding(horizontal = 16.dp),
    )
}

@Composable
private fun Recommendations(
    recommendations: List<MovieResultModel>,
    navigateToMovieDetails: (id: Int) -> Unit,
) {
    Text(
        text = stringResource(id = Strings.recommendations),
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.titleMedium,
    )
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        content = {
            items(recommendations) {
                MediaGridItem(
                    result = it,
                    onClick = navigateToMovieDetails,
                )
            }
        },
    )
}
