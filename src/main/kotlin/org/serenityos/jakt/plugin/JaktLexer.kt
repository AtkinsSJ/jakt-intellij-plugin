package org.serenityos.jakt.plugin

import com.intellij.lexer.Lexer
import com.intellij.lexer.LexerPosition
import com.intellij.openapi.util.text.Strings
import com.intellij.psi.tree.IElementType
import org.serenityos.jakt.bindings.JaktC
import org.serenityos.jakt.bindings.types.Span
import org.serenityos.jakt.bindings.types.Token
import org.serenityos.jakt.bindings.types.TokenContents

class JaktLexer : Lexer() {
    private var buffer: CharSequence = Strings.EMPTY_CHAR_SEQUENCE
    private val tokens = mutableListOf<Token>()
    private var startOffset = 0
    private var endOffset = 0
    private var position = 0

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.startOffset = startOffset
        this.endOffset = endOffset
        this.position = 0

        // TODO: Do something with the error if it exists?
        val range = startOffset..endOffset
        val newTokens = JaktC.lex(buffer.toString()).tokens.filter {
            it.span.start in range || it.span.end in range
        }.toMutableList()

        tokens.clear()

        newTokens.forEachIndexed { index, token ->
            tokens.add(token)
            if (index != newTokens.lastIndex) {
                val next = newTokens[index + 1]
                if (token.span.end != next.span.start) {
                    tokens.add(
                        Token(
                        TokenContents.Whitespace,
                        Span(token.span.fileId, token.span.end, next.span.start),
                    )
                    )
                }
            }
        }
    }

    override fun getState() = 0

    override fun getTokenType(): IElementType? {
        if (position > tokens.lastIndex)
            return null

        return when (val type = tokens[position].contents) {
            TokenContents.Ampersand,
            TokenContents.AmpersandEqual,
            TokenContents.Asterisk,
            TokenContents.AsteriskEqual,
            TokenContents.Caret,
            TokenContents.CaretEqual,
            TokenContents.Colon,
            TokenContents.ColonColon,
            TokenContents.DotDot,
            TokenContents.DoubleEqual,
            TokenContents.Equal,
            TokenContents.ExclamationPoint,
            TokenContents.FatArrow,
            TokenContents.ForwardSlash,
            TokenContents.ForwardSlashEqual,
            TokenContents.GreaterThan,
            TokenContents.GreaterThanOrEqual,
            TokenContents.LeftArithmeticShift,
            TokenContents.LeftShift,
            TokenContents.LeftShiftEqual,
            TokenContents.LessThan,
            TokenContents.LessThanOrEqual,
            TokenContents.Minus,
            TokenContents.MinusEqual,
            TokenContents.MinusMinus,
            TokenContents.NotEqual,
            TokenContents.PercentSign,
            TokenContents.PercentSignEqual,
            TokenContents.Pipe,
            TokenContents.PipeEqual,
            TokenContents.Plus,
            TokenContents.PlusEqual,
            TokenContents.PlusPlus,
            TokenContents.QuestionMark,
            TokenContents.QuestionMarkQuestionMark,
            TokenContents.RightArithmeticShift,
            TokenContents.RightShift,
            TokenContents.RightShiftEqual,
            TokenContents.Tilde -> OperatorType
            TokenContents.Eof,
            TokenContents.Eol,
            TokenContents.Whitespace -> WhitespaceType
            TokenContents.Comma -> CommaType
            TokenContents.Dot -> DotType
            TokenContents.Garbage -> GarbageType
            TokenContents.LCurly,
            TokenContents.RCurly -> CurlyBracketType
            TokenContents.LParen,
            TokenContents.RParen -> ParenType
            TokenContents.LSquare,
            TokenContents.RSquare -> BracketType
            TokenContents.Semicolon -> SemicolonType
            is TokenContents.Name -> NameType(type.value)
            is TokenContents.Number -> NumberType(type.value.value)
            is TokenContents.QuotedString -> QuotedStringType(type.value)
            is TokenContents.SingleQuotedString -> SingleQuotedStringType(type.value.codePointAt(0))
        }
    }

    override fun getTokenStart() = tokens[position].span.start

    // Account for missing whitespace, as the end of one token must be equal to the
    // start of the next token
    override fun getTokenEnd() = if (position == tokens.lastIndex) {
        tokens.last().span.end
    } else {
        tokens[position + 1].span.start
    }

    override fun advance() {
        if (position >= endOffset)
            return

        position++
    }

    override fun getCurrentPosition() = Position(position)

    override fun restore(position: LexerPosition) {
        this.position = position.offset
    }

    override fun getBufferSequence() = buffer

    override fun getBufferEnd() = endOffset

    class Position(private val offset: Int) : LexerPosition {
        override fun getOffset() = offset
        override fun getState() = 0
    }

    sealed class JaktElementType(debugName: String) : IElementType(debugName, JaktLanguage)

    class SingleQuotedStringType(codepoint: Int) : JaktElementType("SingleQuotedString{${buildString { appendCodePoint(codepoint) }}")

    class QuotedStringType(string: String) : JaktElementType("QuotedString{$string}")

    class NumberType(number: Number) : JaktElementType("NumberType{$number}")

    class NameType(val name: String) : JaktElementType("NameType{$name}")

    object ParenType : JaktElementType("ParenType")

    object BracketType : JaktElementType("BracketType")

    object CurlyBracketType : JaktElementType("CurlyBracketType")

    object DotType : JaktElementType("DotType")

    object CommaType : JaktElementType("CommaType")

    object SemicolonType : JaktElementType("Semicolon")

    object OperatorType : JaktElementType("OperatorType")

    object WhitespaceType : JaktElementType("Whitespace")

    object GarbageType : JaktElementType("GarbageType")

}
