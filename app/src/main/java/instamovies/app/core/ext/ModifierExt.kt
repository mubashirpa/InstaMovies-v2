package instamovies.app.core.ext

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.util.lerp
import kotlin.math.absoluteValue

/**
 * Apply a custom item scroll effect to the pager items
 *
 * @param page Index of the current page
 * @param state The state to control this pager
 */
@OptIn(ExperimentalFoundationApi::class)
fun Modifier.carousalTransition(
    page: Int,
    state: PagerState,
) = graphicsLayer {
    val pageOffset = calculateCurrentOffsetForPage(page, state)

    // We animate the scaleX + scaleY, between 85% and 100%
    lerp(
        start = 0.85F,
        stop = 1F,
        fraction = 1F - pageOffset.coerceIn(0F, 1F),
    ).also { scale ->
        scaleX = scale
        scaleY = scale
    }

    // We animate the alpha, between 50% and 100%
    alpha =
        lerp(
            start = 0.5F,
            stop = 1F,
            fraction = 1F - pageOffset.coerceIn(0F, 1F),
        )
}

/**
 * Calculate the absolute offset for the current page from the scroll position.
 *
 * @param page Index of the current page
 * @param state The state to control this pager
 */
@OptIn(ExperimentalFoundationApi::class)
private fun calculateCurrentOffsetForPage(
    page: Int,
    state: PagerState,
): Float {
    // Calculate the absolute offset for the current page from the
    // scroll position. We use the absolute value which allows us to mirror
    // any effects for both directions.
    return ((state.currentPage - page) + state.currentPageOffsetFraction).absoluteValue
}

/**
 * Configure component to receive clicks via input or accessibility "click" event.
 *
 * Add this modifier to the element to make it clickable within its bounds and show no indication.
 *
 * @param enabled Controls the enabled state. When `false`, [onClick], and this modifier will
 * @param onClick will be called when user clicks on the element.
 */
fun Modifier.clickable(
    enabled: Boolean,
    onClick: () -> Unit,
) = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        enabled = enabled,
        onClick = onClick,
    )
}

/**
 * Creates a vertical gradient with the given colors evenly dispersed within the gradient.
 *
 * @param colors colors Colors to be rendered as part of the gradient
 */
fun Modifier.scrim(colors: List<Color>): Modifier =
    drawWithContent {
        drawContent()
        drawRect(Brush.verticalGradient(colors))
    }
