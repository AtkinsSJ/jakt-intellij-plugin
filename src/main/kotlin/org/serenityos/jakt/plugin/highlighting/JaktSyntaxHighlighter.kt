package org.serenityos.jakt.plugin.highlighting

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.tree.IElementType
import org.serenityos.jakt.plugin.JaktLexer

class JaktSyntaxHighlighter: SyntaxHighlighterBase() {
    override fun getHighlightingLexer() = JaktLexer()

    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> {
        return when (tokenType) {
            is JaktLexer.NameType -> {
                when (tokenType.name) {
                    in literalKeywords -> LITERAL_KEY
                    in controlFlowKeywords -> CONTROL_FLOW_KEY
                    in modifiersKeywords -> MODIFIER_KEY
                    in miscKeywords -> MISC_KEY
                    "unsafe" -> UNSAFE_KEY
                    else -> DefaultLanguageHighlighterColors.IDENTIFIER
                }
            }
            JaktLexer.BracketType -> BRACKETS_KEY
            JaktLexer.CommaType -> COMMA_KEY
            JaktLexer.CurlyBracketType -> BRACES_KEY
            JaktLexer.DotType -> DOT_KEY
            JaktLexer.WhitespaceType -> DefaultLanguageHighlighterColors.METADATA
            JaktLexer.GarbageType -> ERROR_KEY
            is JaktLexer.NumberType -> NUMBERS_KEY
            JaktLexer.OperatorType -> OPERATOR_KEY
            JaktLexer.ParenType -> PARENS_KEY
            is JaktLexer.QuotedStringType -> STRINGS_KEY
            JaktLexer.SemicolonType -> SEMICOLON_KEY
            is JaktLexer.SingleQuotedStringType -> CHAR_KEY
            else -> return arrayOf()
        }.let { arrayOf(it) }
    }

    companion object {
        private val literalKeywords = setOf("true", "false", "this")
        private val controlFlowKeywords = setOf(
            "if",
            "else",
            "break",
            "continue",
            "while",
            "loop",
            "return",
            "for",
            "try",
            "catch",
            "match",
            "defer",
            "let",
            "cpp",
        )
        private val modifiersKeywords = setOf(
            "function",
            "mutable",
            "throws",
            "anonymous",
        )
        private val miscKeywords = setOf(
            "is",
            "as",
            "truncated",
            "saturated",
            "in",
            "and",
            "or",
            "not",
        )

        val BRACES_KEY = createTextAttributesKey("JAKT_BRACES_KEY", DefaultLanguageHighlighterColors.BRACES)
        val BRACKETS_KEY = createTextAttributesKey("JAKT_BRACKETS_KEY", DefaultLanguageHighlighterColors.BRACKETS)
        val PARENS_KEY = createTextAttributesKey("JAKT_PARENS_KEY", DefaultLanguageHighlighterColors.PARENTHESES)
        val SEMICOLON_KEY = createTextAttributesKey("JAKT_SEMICOLON_KEY", DefaultLanguageHighlighterColors.SEMICOLON)

        val DOT_KEY = createTextAttributesKey("JAKT_DOT_KEY", DefaultLanguageHighlighterColors.DOT)
        val COMMA_KEY = createTextAttributesKey("JAKT_COMMA_KEY", DefaultLanguageHighlighterColors.COMMA)
        val OPERATOR_KEY = createTextAttributesKey("JAKT_OPERATOR_KEY", DefaultLanguageHighlighterColors.OPERATION_SIGN)

        val NUMBERS_KEY = createTextAttributesKey("JAKT_NUMBERS_KEY", DefaultLanguageHighlighterColors.NUMBER)
        val STRINGS_KEY = createTextAttributesKey("JAKT_STRINGS_KEY", DefaultLanguageHighlighterColors.STRING)
        val CHAR_KEY = createTextAttributesKey("JAKT_CHAR_KEY", DefaultLanguageHighlighterColors.STRING)

        val LITERAL_KEY = createTextAttributesKey("JAKT_LITERAL_KEY", DefaultLanguageHighlighterColors.IDENTIFIER)
        val CONTROL_FLOW_KEY = createTextAttributesKey("JAKT_CONTROL_FLOW_KEY", DefaultLanguageHighlighterColors.IDENTIFIER)
        val MODIFIER_KEY = createTextAttributesKey("JAKT_MODIFIER_KEY", DefaultLanguageHighlighterColors.IDENTIFIER)
        val MISC_KEY = createTextAttributesKey("JAKT_MISC_KEY", DefaultLanguageHighlighterColors.IDENTIFIER)
        val UNSAFE_KEY = createTextAttributesKey("JAKT_UNSAFE_KEY", DefaultLanguageHighlighterColors.IDENTIFIER)
        val ERROR_KEY = createTextAttributesKey("JAKT_ERROR_KEY", HighlighterColors.BAD_CHARACTER)
    }
}

class JaktSyntaxHighlighterFactory : SyntaxHighlighterFactory() {
    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?): SyntaxHighlighter {
        return JaktSyntaxHighlighter()
    }
}

