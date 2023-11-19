package instamovies.app.core.ext

import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable

/**
 * Adds a item which takes the full row
 *
 * @param key a stable and unique key representing the item. Using the same key
 * for multiple items in the grid is not allowed. Type of the key should be saveable
 * via Bundle on Android. If null is passed the position in the grid will represent the key.
 * When you specify the key the scroll position will be maintained based on the key, which
 * means if you add/remove items before the current visible item the item with the given key
 * will be kept as the first visible one.
 * @param contentType the type of the content of this item. The item compositions of the same
 * type could be reused more efficiently. Note that null is a valid type and items of such
 * type will be considered compatible.
 * @param content the content of the item
 */
fun LazyGridScope.header(
    key: Any? = null,
    contentType: Any? = null,
    content: @Composable (LazyGridItemScope.() -> Unit),
) {
    item(
        key = key,
        span = { GridItemSpan(maxLineSpan) },
        contentType = contentType,
        content = content,
    )
}

/**
 * Adds a [count] of empty items
 *
 * @param count the items count
 */
fun LazyGridScope.offsetCells(count: Int) {
    item(
        span = { GridItemSpan(count) },
        content = {},
    )
}
