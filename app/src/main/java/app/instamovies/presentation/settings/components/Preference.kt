package app.instamovies.presentation.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun Preference(
    onClick: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    summary: String? = null,
    icon: ImageVector? = null,
) {
    ListItem(
        headlineContent = {
            Text(text = title)
        },
        modifier = modifier.clickable(onClick = onClick),
        supportingContent =
            summary?.let {
                {
                    Text(text = summary)
                }
            },
        leadingContent =
            icon?.let {
                {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                    )
                }
            },
    )
}
