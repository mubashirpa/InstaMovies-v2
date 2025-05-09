package app.instamovies.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.instamovies.presentation.theme.InstaMoviesTheme
import app.instamovies.R.string as Strings

@Composable
fun ComingSoon(modifier: Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(id = Strings.title_coming_soon),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = stringResource(id = Strings.message_coming_soon),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.outline,
        )
    }
}

@Preview
@Composable
private fun ComingSoonPreview() {
    InstaMoviesTheme {
        Surface {
            ComingSoon(modifier = Modifier.fillMaxSize())
        }
    }
}
