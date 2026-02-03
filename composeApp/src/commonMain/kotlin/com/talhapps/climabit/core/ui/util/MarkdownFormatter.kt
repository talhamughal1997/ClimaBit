package com.talhapps.climabit.core.ui.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

object MarkdownFormatter {
    fun formatMarkdownToAnnotatedString(markdown: String): AnnotatedString {
        return buildAnnotatedString {
            var text = markdown
            val spans = mutableListOf<Triple<Int, Int, SpanStyle>>()

            val boldRegex = Regex("\\*\\*(.*?)\\*\\*")
            val italicRegex = Regex("\\*(.*?)\\*")
            val codeRegex = Regex("`(.*?)`")

            fun processMatches(regex: Regex, style: SpanStyle) {
                var searchStart = 0
                while (true) {
                    val match = regex.find(text, searchStart) ?: break
                    val content = match.groupValues[1]
                    val start = match.range.first
                    val end = match.range.last + 1

                    text = text.replaceRange(start, end, content)
                    val contentStart = start
                    val contentEnd = contentStart + content.length
                    spans.add(Triple(contentStart, contentEnd, style))

                    searchStart = contentEnd
                }
            }

            processMatches(boldRegex, SpanStyle(fontWeight = FontWeight.Bold))
            processMatches(italicRegex, SpanStyle(fontStyle = FontStyle.Italic))
            processMatches(
                codeRegex,
                SpanStyle(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
            )

            append(text)

            spans.sortedBy { it.first }.forEach { (start, end, style) ->
                if (start >= 0 && end <= text.length && start < end) {
                    addStyle(style, start, end)
                }
            }
        }
    }
}

