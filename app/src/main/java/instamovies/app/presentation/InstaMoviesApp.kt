package instamovies.app.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import instamovies.app.core.util.DevicePosture
import instamovies.app.core.util.InstaMoviesContentType
import instamovies.app.core.util.InstaMoviesNavigationContentPosition
import instamovies.app.core.util.InstaMoviesNavigationType
import instamovies.app.core.util.isBookPosture
import instamovies.app.core.util.isSeparating
import instamovies.app.navigation.InstaMoviesNavHost
import instamovies.app.presentation.components.InstaMoviesSnackbarHost

@Composable
fun InstaMoviesApp(
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
) {
    /**
     * This will help us select type of navigation and content type depending on window size and
     * fold state of the device.
     */
    val navigationType: InstaMoviesNavigationType
    val contentType: InstaMoviesContentType

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

    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            navigationType = InstaMoviesNavigationType.BOTTOM_NAVIGATION
            contentType = InstaMoviesContentType.SINGLE_PANE
        }

        WindowWidthSizeClass.Medium -> {
            navigationType = InstaMoviesNavigationType.NAVIGATION_RAIL
            contentType =
                if (foldingDevicePosture != DevicePosture.NormalPosture) {
                    InstaMoviesContentType.DUAL_PANE
                } else {
                    InstaMoviesContentType.SINGLE_PANE
                }
        }

        WindowWidthSizeClass.Expanded -> {
            navigationType =
                if (foldingDevicePosture is DevicePosture.BookPosture) {
                    InstaMoviesNavigationType.NAVIGATION_RAIL
                } else {
                    InstaMoviesNavigationType.PERMANENT_NAVIGATION_DRAWER
                }
            contentType = InstaMoviesContentType.DUAL_PANE
        }

        else -> {
            navigationType = InstaMoviesNavigationType.BOTTOM_NAVIGATION
            contentType = InstaMoviesContentType.SINGLE_PANE
        }
    }

    /**
     * Content inside Navigation Rail/Drawer can also be positioned at top, bottom or center for
     * ergonomics and reachability depending upon the height of the device.
     */
    val navigationContentPosition =
        when (windowSize.heightSizeClass) {
            WindowHeightSizeClass.Compact -> {
                InstaMoviesNavigationContentPosition.TOP
            }

            WindowHeightSizeClass.Medium, WindowHeightSizeClass.Expanded -> {
                InstaMoviesNavigationContentPosition.CENTER
            }

            else -> {
                InstaMoviesNavigationContentPosition.TOP
            }
        }

    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize().imePadding(),
        snackbarHost = {
            Box(modifier = Modifier.fillMaxWidth()) {
                InstaMoviesSnackbarHost(hostState = snackbarHostState)
            }
        },
        contentWindowInsets = WindowInsets(0),
    ) { innerPadding ->
        InstaMoviesContent(
            navController = navController,
            widthSizeClass = windowSize.widthSizeClass,
            navigationType = navigationType,
            contentType = contentType,
            displayFeatures = displayFeatures,
            navigationContentPosition = navigationContentPosition,
            snackbarHostState = snackbarHostState,
            modifier =
                Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)),
        )
    }
}

@Composable
private fun InstaMoviesContent(
    navController: NavHostController,
    widthSizeClass: WindowWidthSizeClass,
    navigationType: InstaMoviesNavigationType,
    contentType: InstaMoviesContentType,
    displayFeatures: List<DisplayFeature>,
    navigationContentPosition: InstaMoviesNavigationContentPosition,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    InstaMoviesNavHost(
        navController = navController,
        widthSizeClass = widthSizeClass,
        contentType = contentType,
        displayFeatures = displayFeatures,
        navigationContentPosition = navigationContentPosition,
        navigationType = navigationType,
        snackbarHostState = snackbarHostState,
        modifier = modifier,
    )
}
