package instamovies.app.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import instamovies.app.R.string as Strings

private const val TAG_SHOW_MORE = "showMore"
private const val TAG_SHOW_LESS = "showLess"

/**
 * Displays text with an expandable and collapsible option based on the number of lines.
 *
 * @param text the text to be displayed.
 * @param maxLines the maximum number of lines to display before truncating.
 * @param modifier The [Modifier] to be applied to this layout node.
 * @param color [Color] to apply to the text. If [Color.Unspecified], and [style] has no color set,
 *   this will be [LocalContentColor].
 * @param fontSize the size of glyphs to use when painting the text. See [TextStyle.fontSize].
 * @param textAlign the alignment of the text within the lines of the paragraph. See
 *   [TextStyle.textAlign].
 * @param showMoreLabel the label for the "Show more" link.
 * @param showLessLabel the label for the "Show less" link.
 * @param style style style configuration for the text such as color, font, line height etc.
 */
@Composable
fun ExpandableText(
    text: String,
    maxLines: Int,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign? = null,
    showMoreLabel: String = stringResource(id = Strings.label_show_more),
    showLessLabel: String = stringResource(id = Strings.label_show_less),
    style: TextStyle = LocalTextStyle.current,
) {
    val textColor = color.takeOrElse { style.color.takeOrElse { LocalContentColor.current } }

    var isExpanded by remember { mutableStateOf(false) }
    var truncatedText by remember { mutableStateOf(text) }
    var needsTruncation by remember { mutableStateOf(false) }
    val showMore = " $showMoreLabel"
    val showLess = " $showLessLabel"

    LaunchedEffect(text, maxLines) {
        needsTruncation = false
        truncatedText = text
    }

    val annotatedText =
        buildAnnotatedString {
            val textStyle = SpanStyle(color = textColor, fontSize = fontSize)

            withStyle(style = textStyle) {
                if (isExpanded) {
                    append(text)
                } else {
                    append(truncatedText)
                }
            }

            val linkStyle =
                SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = fontSize,
                )
            val linkTag = if (isExpanded) TAG_SHOW_LESS else TAG_SHOW_MORE

            withLink(
                LinkAnnotation.Clickable(
                    tag = linkTag,
                    styles = TextLinkStyles(style = linkStyle),
                    linkInteractionListener = {
                        isExpanded = !isExpanded
                    },
                ),
            ) {
                append(if (isExpanded) showLess else showMore)
            }
        }

    Text(
        text = annotatedText,
        modifier = modifier.animateContentSize(),
        maxLines = if (isExpanded) Int.MAX_VALUE else maxLines,
        onTextLayout = { result ->
            if (!isExpanded && result.hasVisualOverflow) {
                needsTruncation = true
                val lastCharIndex = result.getLineEnd(lineIndex = maxLines - 1)
                truncatedText =
                    text
                        .substring(startIndex = 0, endIndex = lastCharIndex)
                        .dropLast(showMore.length)
                        .dropLastWhile { it.isWhitespace() || it == '.' }
                        .plus("...")
            }
        },
        style = style.merge(textAlign = textAlign ?: TextAlign.Unspecified),
    )
}
