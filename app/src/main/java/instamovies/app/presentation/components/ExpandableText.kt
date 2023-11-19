package instamovies.app.presentation.components

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import instamovies.app.R.string as Strings

private const val TAG_SHOW_MORE = "showMore"
private const val TAG_SHOW_LESS = "showLess"

/**
 * Displays text content with an option to expand and collapse the text based on the number of lines.
 *
 * @param text The text to be displayed.
 * @param maxLines The maximum number of lines to display before truncating the text.
 * @param modifier The [Modifier] to be applied to this layout node.
 * @param color [Color] to apply to the text.
 * @param fontSize The size of glyphs to use when painting the text. See [TextStyle.fontSize].
 * @param textAlign The alignment of the text within the lines of the paragraph.
 * See [TextStyle.textAlign].
 * @param showMoreLabel The label for the "Show more" link.
 * @param showLessLabel The label for the "Show less" link.
 * @param style Style configuration for the text such as color, font, line height etc.
 */
@Composable
fun ExpandableText(
    text: String,
    maxLines: Int,
    modifier: Modifier = Modifier,
    color: Color = LocalTextStyle.current.color,
    fontSize: TextUnit = LocalTextStyle.current.fontSize,
    textAlign: TextAlign = TextAlign.Unspecified,
    showMoreLabel: String = stringResource(id = Strings.label_show_more),
    showLessLabel: String = stringResource(id = Strings.label_show_less),
    style: TextStyle = LocalTextStyle.current,
) {
    var isExpanded by remember { mutableStateOf(value = false) }
    val hasVisualOverflow = remember { mutableStateOf(value = false) }
    var adjustedText by remember { mutableStateOf(value = text) }
    val showMore = " $showMoreLabel"
    val showLess = " $showLessLabel"

    val annotatedText =
        buildAnnotatedString {
            if (isExpanded) {
                withStyle(style = SpanStyle(color = color, fontSize = fontSize)) {
                    append(text)
                }
                pushStringAnnotation(tag = TAG_SHOW_LESS, annotation = TAG_SHOW_LESS)
                append(showLess)
                addStyle(
                    style =
                        SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = fontSize,
                        ),
                    start = text.length,
                    end = text.length + showMore.length,
                )
                pop()
            } else {
                withStyle(style = SpanStyle(color = color, fontSize = fontSize)) {
                    append(adjustedText)
                }
                if (hasVisualOverflow.value) {
                    pushStringAnnotation(tag = TAG_SHOW_MORE, annotation = TAG_SHOW_MORE)
                    append(showMore)
                    addStyle(
                        style =
                            SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = fontSize,
                            ),
                        start = adjustedText.length,
                        end = adjustedText.length + showMore.length,
                    )
                    pop()
                }
            }
        }

    ClickableText(
        text = annotatedText,
        modifier = modifier,
        style = style.copy(textAlign = textAlign),
        maxLines = if (isExpanded) Int.MAX_VALUE else maxLines,
        onTextLayout = { result ->
            if (!isExpanded && result.hasVisualOverflow) {
                hasVisualOverflow.value = true
                val lastCharIndex = result.getLineEnd(lineIndex = maxLines - 1)
                adjustedText =
                    text
                        .substring(startIndex = 0, endIndex = lastCharIndex)
                        .dropLast(showMore.length)
                        .dropLastWhile { it == ' ' || it == '.' }
                        .plus("...")
            }
        },
        onClick = { offset ->
            annotatedText.getStringAnnotations(
                tag = TAG_SHOW_LESS,
                start = offset,
                end = offset + showLess.length,
            ).firstOrNull()?.let {
                isExpanded = !isExpanded
            }
            annotatedText.getStringAnnotations(
                tag = TAG_SHOW_MORE,
                start = offset,
                end = offset + showMore.length,
            ).firstOrNull()?.let {
                isExpanded = !isExpanded
            }
        },
    )
}
