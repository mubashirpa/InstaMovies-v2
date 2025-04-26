package instamovies.app.presentation.components

import android.content.res.ColorStateList
import android.widget.RatingBar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.StarHalf
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import instamovies.app.presentation.theme.InstaMoviesTheme
import kotlin.math.ceil
import kotlin.math.floor
import instamovies.app.R.color as Colors

@Composable
fun RatingBar(
    rating: Double,
    modifier: Modifier = Modifier,
    stars: Int = 5,
    starColor: Color = MaterialTheme.colorScheme.primary,
    starSize: Dp = 20.dp,
) {
    val tempRating = rating.coerceAtMost(stars.toDouble())
    val filledStars = floor(tempRating).toInt()
    val unfilledStars = (stars - ceil(tempRating)).toInt()
    val halfStar = tempRating.rem(1) != 0.0

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(filledStars) {
            Icon(
                Icons.Outlined.Star,
                contentDescription = null,
                modifier = Modifier.size(starSize),
                tint = starColor,
            )
        }

        if (halfStar) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.StarHalf,
                contentDescription = null,
                modifier = Modifier.size(starSize),
                tint = starColor,
            )
        }

        repeat(unfilledStars) {
            Icon(
                imageVector = Icons.Outlined.StarOutline,
                contentDescription = null,
                modifier = Modifier.size(starSize),
                tint = Color.DarkGray,
            )
        }
    }
}

@Composable
fun RatingBar(
    rating: Double,
    onRatingChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    isIndicator: Boolean = false,
    numStars: Int = 5,
) {
    AndroidView(
        factory = { context ->
            RatingBar(context).apply {
                setIsIndicator(isIndicator)
                this.numStars = numStars
                progressTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(context, Colors.md_theme_primary))
                setOnRatingBarChangeListener { _, rating, _ ->
                    onRatingChange(rating)
                }
            }
        },
        modifier = modifier,
        update = { ratingBar ->
            ratingBar.rating = rating.toFloat()
        },
    )
}

@Preview
@Composable
private fun RatingBarPreview() {
    InstaMoviesTheme {
        Column {
            RatingBar(rating = 4.5)
            Spacer(modifier = Modifier.height(10.dp))
            RatingBar(rating = 3.4, onRatingChange = {})
        }
    }
}
