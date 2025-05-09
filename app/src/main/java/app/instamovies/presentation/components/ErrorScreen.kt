package app.instamovies.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
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
fun ErrorScreen(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    direction: Axis = Axis.Vertical,
    message: String = stringResource(id = Strings.error_unknown),
    button: String = stringResource(id = Strings.retry),
) {
    when (direction) {
        Axis.Horizontal -> {
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = message,
                    modifier = Modifier.weight(weight = 1F, fill = false),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.width(24.dp))
                Button(onClick = onRetry) {
                    Text(text = button)
                }
            }
        }

        Axis.Vertical -> {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = message,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onRetry) {
                    Text(text = button)
                }
            }
        }
    }
}

enum class Axis {
    Horizontal,
    Vertical,
}

@Preview(showBackground = true)
@Composable
private fun ErrorScreenPreview() {
    InstaMoviesTheme {
        ErrorScreen(
            onRetry = {},
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            direction = Axis.Horizontal,
        )
    }
}
