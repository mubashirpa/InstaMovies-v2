package app.instamovies.presentation.settings

import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import app.instamovies.R
import app.instamovies.core.Constants
import app.instamovies.domain.model.preferences.AppTheme
import app.instamovies.presentation.components.BackButton
import app.instamovies.presentation.settings.components.ListPreference
import app.instamovies.presentation.settings.components.Preference
import app.instamovies.presentation.theme.InstaMoviesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onEvent: (SettingsUiEvent) -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val themes = stringArrayResource(id = R.array.app_theme).toList()
    val themeValues = AppTheme.entries.toList().map { it.name }
    val uiModeManager =
        remember(context) { context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.title_settings_screen))
                },
                navigationIcon = {
                    BackButton(onClick = onNavigateUp)
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding),
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ListPreference(
                    title = stringResource(id = R.string.theme),
                    entries = themes,
                    entryValues = themeValues,
                    defaultValue = uiState.selectedTheme.name,
                    summary = themes[themeValues.indexOf(uiState.selectedTheme.name)],
                    icon = Icons.Default.BrightnessMedium,
                    onConfirmClick = { _, value ->
                        val appTheme = enumValueOf<AppTheme>(value)
                        onEvent(SettingsUiEvent.OnAppThemeChange(appTheme))

                        when (appTheme) {
                            AppTheme.SYSTEM_DEFAULT -> {
                                uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_AUTO)
                            }

                            AppTheme.LIGHT -> {
                                uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_NO)
                            }

                            AppTheme.DARK -> {
                                uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_YES)
                            }
                        }
                    },
                )
            }
            Preference(
                onClick = {
                    sendEmail(context)
                },
                title = stringResource(R.string.support),
                icon = Icons.Default.SupportAgent,
            )
        }
    }
}

private fun sendEmail(context: Context) {
    val intent =
        Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri() // only email apps should handle this
            putExtra(Intent.EXTRA_EMAIL, arrayOf(Constants.SUPPORT_EMAIL))
        }
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    InstaMoviesTheme {
        SettingsScreen(
            uiState = SettingsUiState(),
            onEvent = {},
            onNavigateUp = {},
        )
    }
}
