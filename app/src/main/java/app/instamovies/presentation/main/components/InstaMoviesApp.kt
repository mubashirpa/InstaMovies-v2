package app.instamovies.presentation.main.components

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import app.instamovies.core.util.DevicePosture
import app.instamovies.core.util.InstaMoviesContentType
import app.instamovies.core.util.InstaMoviesWindowWidthType
import app.instamovies.core.util.isBookPosture
import app.instamovies.core.util.isSeparating
import app.instamovies.navigation.InstaMoviesNavHost

@Composable
fun InstaMoviesApp(
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    /**
     * We are using display's folding features to map the device postures a fold is in.
     * In the state of folding device If it's half fold in BookPosture we want to avoid content
     * at the crease/hinge
     */
    val foldingFeature = displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()

    val foldingDevicePosture =
        when {
            isBookPosture(foldingFeature) -> DevicePosture.BookPosture(foldingFeature.bounds)

            isSeparating(foldingFeature) ->
                DevicePosture.Separating(
                    foldingFeature.bounds,
                    foldingFeature.orientation,
                )

            else -> DevicePosture.NormalPosture
        }

    val contentType =
        when (windowSize.widthSizeClass) {
            WindowWidthSizeClass.Compact -> InstaMoviesContentType.SINGLE_PANE

            WindowWidthSizeClass.Medium ->
                if (foldingDevicePosture != DevicePosture.NormalPosture) {
                    InstaMoviesContentType.DUAL_PANE
                } else {
                    InstaMoviesContentType.SINGLE_PANE
                }

            WindowWidthSizeClass.Expanded -> InstaMoviesContentType.DUAL_PANE

            else -> InstaMoviesContentType.SINGLE_PANE
        }

    val windowWidthType =
        when (windowSize.widthSizeClass) {
            WindowWidthSizeClass.Medium -> InstaMoviesWindowWidthType.MEDIUM
            WindowWidthSizeClass.Expanded -> InstaMoviesWindowWidthType.EXPANDED
            else -> InstaMoviesWindowWidthType.COMPACT
        }

    InstaMoviesNavHost(
        navController = navController,
        contentType = contentType,
        windowWidthType = windowWidthType,
        displayFeatures = displayFeatures,
        modifier = modifier,
    )
}
