package instamovies.app.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink

private const val DEFAULT_MINIMUM_TEXT_LINE = 3
private const val TAG_SHOW_MORE = "showMore"
private const val TAG_SHOW_LESS = "showLess"

@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = DEFAULT_MINIMUM_TEXT_LINE,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    style: TextStyle = LocalTextStyle.current,
) {
    var isExpanded by remember { mutableStateOf(false) }
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    var textWithMoreLess by remember { mutableStateOf(buildAnnotatedString { append(text) }) }
    val linkStyle = SpanStyle(color = MaterialTheme.colorScheme.primary)

    LaunchedEffect(textLayoutResult) {
        if (textLayoutResult == null) return@LaunchedEffect

        when {
            isExpanded -> {
                textWithMoreLess =
                    buildAnnotatedString {
                        append(text)
                        withLink(
                            LinkAnnotation.Clickable(
                                tag = TAG_SHOW_LESS,
                                styles = TextLinkStyles(style = linkStyle),
                                linkInteractionListener = { isExpanded = !isExpanded },
                            ),
                        ) {
                            append(" Show less")
                        }
                    }
            }

            !isExpanded && textLayoutResult!!.hasVisualOverflow -> {
                val lastCharIndex = textLayoutResult!!.getLineEnd(maxLines - 1)
                val showMoreString = "...Show more"

                val safeEndIndex =
                    text.lastIndexOf(' ', startIndex = lastCharIndex - showMoreString.length)
                val adjustedText =
                    if (safeEndIndex > 0) {
                        text.substring(0, safeEndIndex)
                    } else {
                        text.substring(0, lastCharIndex)
                    }

                textWithMoreLess =
                    buildAnnotatedString {
                        append(adjustedText)
                        withLink(
                            LinkAnnotation.Clickable(
                                tag = TAG_SHOW_MORE,
                                styles = TextLinkStyles(style = linkStyle),
                                linkInteractionListener = { isExpanded = !isExpanded },
                            ),
                        ) {
                            append(showMoreString)
                        }
                    }
            }
        }
    }

    Text(
        text = textWithMoreLess,
        modifier = modifier.animateContentSize(),
        maxLines = if (isExpanded) Int.MAX_VALUE else maxLines,
        color = color,
        textAlign = textAlign,
        onTextLayout = { textLayoutResult = it },
        style = style,
    )
}
