package instamovies.app.presentation.components

import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

/**
 * [SnackbarHost] that is configured for insets and large screens
 */
@Composable
fun InstaMoviesSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    snackbar: @Composable (SnackbarData) -> Unit = { Snackbar(it) },
) {
    val configuration = LocalConfiguration.current
    val smallestScreenWidth = configuration.smallestScreenWidthDp.dp

    SnackbarHost(
        hostState = hostState,
        modifier =
            modifier
                // Limit the Snackbar width for large screens
                .wrapContentWidth(align = Alignment.Start)
                .widthIn(max = smallestScreenWidth),
        snackbar = snackbar,
    )
}
