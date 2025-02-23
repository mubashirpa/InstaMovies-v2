package instamovies.app.presentation.personDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.window.layout.DisplayFeature
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import instamovies.app.core.Constants
import instamovies.app.core.util.DateUtils
import instamovies.app.core.util.InstaMoviesContentType
import instamovies.app.core.util.Resource
import instamovies.app.domain.model.MediaType
import instamovies.app.domain.model.person.PersonDetails
import instamovies.app.domain.model.person.credits.PersonCast
import instamovies.app.domain.model.person.images.PersonProfile
import instamovies.app.presentation.components.BackButton
import instamovies.app.presentation.components.ErrorScreen
import instamovies.app.presentation.components.ExpandableText
import instamovies.app.presentation.components.LoadingIndicator
import instamovies.app.presentation.components.MediaGridItem
import instamovies.app.presentation.personDetails.components.ProfileListItem
import org.jetbrains.annotations.ApiStatus.Experimental
import instamovies.app.R.string as Strings

@Composable
fun PersonDetailsScreen(
    contentType: InstaMoviesContentType,
    displayFeatures: List<DisplayFeature>,
    uiState: PersonDetailsUiState,
    onEvent: (PersonDetailsUiEvent) -> Unit,
    title: String,
    navigateToMovieDetails: (id: Int) -> Unit,
    navigateToTvShowDetails: (id: Int) -> Unit,
    onBackPressed: () -> Unit,
) {
    val personDetailsResource = uiState.personDetailsResource

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        when (personDetailsResource) {
            is Resource.Empty -> {}

            is Resource.Error -> {
                ErrorScreen(
                    onRetry = {
                        onEvent(PersonDetailsUiEvent.OnRetry)
                    },
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                    message = personDetailsResource.message!!.asString(),
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
                val details = personDetailsResource.data
                if (details != null) {
                    val layoutDirection = LocalLayoutDirection.current
                    val contentPadding =
                        PaddingValues(
                            start = innerPadding.calculateStartPadding(layoutDirection),
                            end = innerPadding.calculateEndPadding(layoutDirection),
                            bottom = innerPadding.calculateBottomPadding(),
                        )

                    if (contentType == InstaMoviesContentType.DUAL_PANE) {
                        TwoPane(
                            first = {
                                PersonDetailsDualPainContentFirst(
                                    title = title,
                                    details = details,
                                    onBackPressed = onBackPressed,
                                )
                            },
                            second = {
                                PersonDetailsDualPainContentSecond(
                                    details = details,
                                    navigateToMovieDetails = navigateToMovieDetails,
                                    navigateToTvShowDetails = navigateToTvShowDetails,
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
                        PersonDetailsSinglePainContent(
                            title = title,
                            contentPadding = contentPadding,
                            details = details,
                            navigateToMovieDetails = navigateToMovieDetails,
                            navigateToTvShowDetails = navigateToTvShowDetails,
                            onBackPressed = onBackPressed,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PersonDetailsSinglePainContent(
    title: String,
    contentPadding: PaddingValues,
    details: PersonDetails,
    navigateToMovieDetails: (id: Int) -> Unit,
    navigateToTvShowDetails: (id: Int) -> Unit,
    onBackPressed: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Column(modifier = Modifier.fillMaxSize()) {
        PersonDetailsAppBar(
            title = title,
            scrollBehavior = scrollBehavior,
            onBackPressed = onBackPressed,
        )
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Header(details = details)
                }

                val biography = details.biography
                if (!biography.isNullOrEmpty()) {
                    item { Biography(biography = biography, isDualPane = false) }
                }

                val profiles = details.images?.profiles.orEmpty()
                if (profiles.isNotEmpty()) {
                    item { Photos(profiles = profiles) }
                }

                val casts = details.credits?.cast.orEmpty()
                if (casts.isNotEmpty()) {
                    item {
                        KnownFor(
                            casts = casts,
                            navigateToMovieDetails = navigateToMovieDetails,
                            navigateToTvShowDetails = navigateToTvShowDetails,
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PersonDetailsDualPainContentFirst(
    title: String,
    details: PersonDetails,
    onBackPressed: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = {
            item {
                PersonDetailsAppBar(
                    title = title,
                    windowInsets = WindowInsets(0),
                    onBackPressed = onBackPressed,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Header(details = details)
            }

            val biography = details.biography
            if (!biography.isNullOrEmpty()) {
                item { Biography(biography = biography, isDualPane = true) }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        },
    )
}

@Composable
private fun PersonDetailsDualPainContentSecond(
    details: PersonDetails,
    navigateToMovieDetails: (id: Int) -> Unit,
    navigateToTvShowDetails: (id: Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = {
            val profiles = details.images?.profiles.orEmpty()
            if (profiles.isNotEmpty()) {
                item { Photos(profiles = profiles) }
            }

            val casts = details.credits?.cast.orEmpty()
            if (casts.isNotEmpty()) {
                item {
                    KnownFor(
                        casts = casts,
                        navigateToMovieDetails = navigateToMovieDetails,
                        navigateToTvShowDetails = navigateToTvShowDetails,
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PersonDetailsAppBar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    onBackPressed: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        },
        navigationIcon = {
            BackButton(onClick = onBackPressed)
        },
        windowInsets = windowInsets,
        scrollBehavior = scrollBehavior,
    )
}

@Composable
private fun Header(details: PersonDetails) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
    ) {
        Card {
            AsyncImage(
                model =
                    ImageRequest
                        .Builder(LocalContext.current)
                        .data("${Constants.TMDB_PROFILE_PREFIX}${details.profilePath}")
                        .crossfade(true)
                        .build(),
                contentDescription = null,
                modifier =
                    Modifier
                        .width(100.dp)
                        .aspectRatio(2F / 3F),
                contentScale = ContentScale.FillBounds,
            )
        }
        Column(modifier = Modifier.padding(start = 16.dp)) {
            val knownForDepartment = details.knownForDepartment
            val birthplace = details.placeOfBirth
            val birthday =
                DateUtils.formatStringDateTime(
                    details.birthday.orEmpty(),
                    "yyyy-MM-dd",
                    "dd MMMM yyyy",
                )
            Text(
                text = details.name.orEmpty(),
                style = MaterialTheme.typography.titleMedium,
            )
            if (!knownForDepartment.isNullOrEmpty()) {
                Text(
                    text = stringResource(id = Strings.known_for),
                    modifier =
                        Modifier.padding(
                            top = 8.dp,
                            bottom = 2.dp,
                        ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall,
                )
                Text(
                    text = knownForDepartment,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            if (!birthday.isNullOrEmpty()) {
                Text(
                    text = stringResource(id = Strings.born),
                    modifier =
                        Modifier.padding(
                            top = 8.dp,
                            bottom = 2.dp,
                        ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall,
                )
                Text(
                    text = birthday,
                    style = MaterialTheme.typography.bodyMedium,
                )
                if (!birthplace.isNullOrEmpty()) {
                    Text(
                        text = birthplace,
                        modifier = Modifier.padding(top = 2.dp),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun Biography(
    biography: String,
    isDualPane: Boolean,
) {
    Text(
        text = stringResource(id = Strings.biography),
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.titleMedium,
    )
    ExpandableText(
        text = biography,
        modifier = Modifier.padding(horizontal = 16.dp),
        maxLines = if (isDualPane) Int.MAX_VALUE else 10,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
private fun KnownFor(
    casts: List<PersonCast>,
    navigateToMovieDetails: (id: Int) -> Unit,
    navigateToTvShowDetails: (id: Int) -> Unit,
) {
    Text(
        text = stringResource(id = Strings.known_for),
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.titleMedium,
    )
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        content = {
            items(casts) {
                MediaGridItem(
                    cast = it,
                    onClick = { mediaType, id ->
                        when (mediaType) {
                            MediaType.MOVIE -> navigateToMovieDetails(id)
                            MediaType.TV -> navigateToTvShowDetails(id)
                            else -> {}
                        }
                    },
                )
            }
        },
    )
}

@Experimental
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Photos(profiles: List<PersonProfile>) {
    Text(
        text = stringResource(id = Strings.photos),
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.titleMedium,
    )
    HorizontalUncontainedCarousel(
        state = rememberCarouselState { profiles.count() },
        itemWidth = 186.dp,
        modifier =
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        itemSpacing = 12.dp,
        contentPadding = PaddingValues(horizontal = 16.dp),
    ) { index ->
        val profile = profiles[index]

        ProfileListItem(
            filePath = profile.filePath,
            modifier =
                Modifier
                    .height(205.dp)
                    .maskClip(MaterialTheme.shapes.medium),
        )
    }
}
