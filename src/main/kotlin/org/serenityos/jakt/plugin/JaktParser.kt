package org.serenityos.jakt.plugin

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

class JaktParserDefinition : ParserDefinition {
    override fun createLexer(project: Project?) = JaktLexer()

    override fun createParser(project: Project?) = JaktParser()

    override fun getFileNodeType() = FILE_NODE_TYPE

    override fun getCommentTokens(): TokenSet = COMMENTS

    override fun getStringLiteralElements(): TokenSet = STRING_LITERALS

    override fun getWhitespaceTokens(): TokenSet = WHITE_SPACES

    override fun createFile(viewProvider: FileViewProvider) = JaktFile(viewProvider)

    override fun createElement(node: ASTNode?): PsiElement {

    }

    companion object {
        private val WHITE_SPACES = TokenSet.forAllMatching { it is JaktLexer.WhitespaceType }
        private val COMMENTS = TokenSet.EMPTY // TODO: Jakt doesn't emit comment tokens
        private val STRING_LITERALS = TokenSet.forAllMatching {
            it is JaktLexer.QuotedStringType || it is JaktLexer.SingleQuotedStringType
        }

        private val FILE_NODE_TYPE = IFileElementType(JaktLanguage)
    }
}

class JaktParser : PsiParser {
    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        builder.
    }
}
