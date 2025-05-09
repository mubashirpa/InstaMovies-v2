package app.instamovies.presentation.settings.components

import android.content.res.Resources
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.instamovies.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListPreference(
    onConfirmClick: (Int, String) -> Unit,
    title: String,
    entries: List<String>,
    modifier: Modifier = Modifier,
    entryValues: List<String> = emptyList(),
    defaultValue: String? = null,
    summary: String? = null,
    icon: ImageVector? = null,
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val openDialog = remember { mutableStateOf(false) }
    val defaultSelectedOption =
        if (entryValues.isNotEmpty()) {
            entryValues.indexOf(defaultValue)
        } else {
            entries.indexOf(defaultValue)
        }
    val (selectedOption, onOptionSelected) = remember { mutableIntStateOf(0) }
    val itemHeightPx = remember { 56.dpToPx }

    ListItem(
        headlineContent = {
            Text(text = title)
        },
        modifier =
            modifier.clickable {
                if (defaultSelectedOption >= 0) {
                    onOptionSelected(defaultSelectedOption)
                    if (scrollState.maxValue > 0) {
                        coroutineScope.launch {
                            scrollState.scrollTo((itemHeightPx * defaultSelectedOption))
                        }
                    }
                }
                openDialog.value = true
            },
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

    if (openDialog.value && entries.isNotEmpty()) {
        BasicAlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            modifier = Modifier.padding(vertical = 16.dp),
        ) {
            Surface(
                modifier =
                    Modifier
                        .wrapContentWidth()
                        .wrapContentHeight(),
                shape = AlertDialogDefaults.shape,
                color = AlertDialogDefaults.containerColor,
                tonalElevation = AlertDialogDefaults.TonalElevation,
            ) {
                Column(
                    modifier =
                        Modifier
                            .height(IntrinsicSize.Min)
                            .padding(vertical = 24.dp),
                ) {
                    Text(
                        text = title,
                        modifier = Modifier.padding(horizontal = 24.dp),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (scrollState.canScrollBackward) {
                        HorizontalDivider()
                    }
                    Column(
                        modifier =
                            Modifier
                                .weight(1f)
                                .selectableGroup()
                                .verticalScroll(scrollState),
                    ) {
                        entries.forEachIndexed { index, text ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .selectable(
                                        selected = (index == selectedOption),
                                        onClick = {
                                            onOptionSelected(index)
                                        },
                                        role = Role.RadioButton,
                                    ).padding(horizontal = 24.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                RadioButton(
                                    selected = (index == selectedOption),
                                    onClick = null, // null recommended for accessibility with screen readers
                                )
                                Text(
                                    text = text,
                                    modifier = Modifier.padding(start = 16.dp),
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            }
                        }
                    }
                    if (scrollState.canScrollForward) {
                        HorizontalDivider()
                    }
                    Row(
                        modifier =
                            Modifier
                                .padding(horizontal = 24.dp)
                                .padding(top = 16.dp)
                                .align(Alignment.End),
                    ) {
                        TextButton(onClick = { openDialog.value = false }) {
                            Text(stringResource(id = R.string.cancel))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(
                            onClick = {
                                openDialog.value = false
                                if (entryValues.isNotEmpty()) {
                                    onConfirmClick(
                                        selectedOption,
                                        entryValues[selectedOption],
                                    )
                                } else {
                                    onConfirmClick(
                                        selectedOption,
                                        entries[selectedOption],
                                    )
                                }
                            },
                        ) {
                            Text(stringResource(id = R.string.ok))
                        }
                    }
                }
            }
        }
    }
}

private val Int.dpToPx: Int
    get() {
        val density = Resources.getSystem().displayMetrics.density
        return (this * density + 0.5f).toInt()
    }
