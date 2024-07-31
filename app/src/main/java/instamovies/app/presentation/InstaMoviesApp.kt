package instamovies.app.presentation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import instamovies.app.core.util.DevicePosture
import instamovies.app.core.util.InstaMoviesContentType
import instamovies.app.core.util.InstaMoviesWindowWidthType
import instamovies.app.core.util.isBookPosture
import instamovies.app.core.util.isSeparating
import instamovies.app.navigation.InstaMoviesNavHost

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
            isBookPosture(foldingFeature) -> {
                DevicePosture.BookPosture(foldingFeature.bounds)
            }

            isSeparating(foldingFeature) -> {
                DevicePosture.Separating(foldingFeature.bounds, foldingFeature.orientation)
            }

            else -> {
                DevicePosture.NormalPosture
            }
        }

    val contentType =
        when {
            windowSize.widthSizeClass == WindowWidthSizeClass.Expanded -> {
                InstaMoviesContentType.DUAL_PANE
            }

            windowSize.widthSizeClass == WindowWidthSizeClass.Medium &&
                foldingDevicePosture != DevicePosture.NormalPosture -> {
                InstaMoviesContentType.DUAL_PANE
            }

            else -> {
                InstaMoviesContentType.SINGLE_PANE
            }
        }

    val windowWidthType =
        when (windowSize.widthSizeClass) {
            WindowWidthSizeClass.Medium -> InstaMoviesWindowWidthType.MEDIUM
            WindowWidthSizeClass.Expanded -> InstaMoviesWindowWidthType.EXPANDED
            else -> InstaMoviesWindowWidthType.COMPACT
        }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal),
    ) { innerPadding ->
        InstaMoviesContent(
            navController = navController,
            contentType = contentType,
            windowWidthType = windowWidthType,
            displayFeatures = displayFeatures,
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding),
        )
    }
}

@Composable
private fun InstaMoviesContent(
    navController: NavHostController,
    contentType: InstaMoviesContentType,
    windowWidthType: InstaMoviesWindowWidthType,
    displayFeatures: List<DisplayFeature>,
    modifier: Modifier = Modifier,
) {
    InstaMoviesNavHost(
        navController = navController,
        contentType = contentType,
        windowWidthType = windowWidthType,
        displayFeatures = displayFeatures,
        modifier = modifier,
    )
}
